package com.sandman.blog.service.user;

import com.github.pagehelper.PageHelper;
import com.sandman.blog.dao.mysql.user.BlogDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.common.SftpParam;
import com.sandman.blog.entity.common.SortParam;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.utils.FileUtils;
import com.sandman.blog.utils.PageBean;
import com.sandman.blog.utils.RandomUtils;
import com.sandman.blog.utils.SftpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private BlogDao blogDao;
    public BaseDto getBlogById(Long id){
        Blog blog = blogDao.getBlogById(id);
        return new BaseDto(ResponseStatus.SUCCESS,blog);
    }
    public BaseDto getAllBlog(Integer pageNumber, Integer size){
        log.info("pageNumber:{},,,,,size:{}",pageNumber,size);
        List<Blog> blogList = blogDao.getAllBlog(new SortParam(pageNumber,size));
        log.info(blogList.toString());
        return new BaseDto(ResponseStatus.SUCCESS,blogList);
    }
    public BaseDto findByKeyWord(Integer pageNumber, Integer size,String keyWord){
        log.info("pageNumber======={},size========{},keyword========{}",pageNumber,size,keyWord);
        SortParam sortParam = new SortParam(pageNumber,size);
        if(keyWord!=null && !"".equals(keyWord) && !"undefined".equals(keyWord)){
            sortParam.setCause(keyWord);
        }
        List<Blog> blogList = blogDao.getAllBlog(sortParam);
        if(blogList.size()<1){ // 如果根据keyword查询出来的数据条数 0 ，就不加keyword去查询
            sortParam.setCause(null);
            blogList = blogDao.getAllBlog(sortParam);
        }
        log.info(blogList.toString());
        return new BaseDto(ResponseStatus.SUCCESS,blogList);
    }
    public BaseDto findByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        log.info("pageNumber======={},size========{},bloggerId========{}",pageNumber,size,bloggerId);
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        order = (order==null || "".equals(order))?"createTime":order;
        String orderBy = order + " " + sortType;//默认按照id降序排序

        Integer totalRow = blogDao.findByBloggerId(bloggerId).size();//查询出数据条数
        log.info("totalRow:::::{}",totalRow);
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);

        List<Blog> blogList = blogDao.findByBloggerId(bloggerId);//查询出列表（已经分页）

        PageBean<Blog> pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码

        pageBean.setItems(blogList);
        List<Blog> result = pageBean.getItems();

        Map data = new HashMap();//最终返回的map

        data.put("totalRow",totalRow);
        data.put("totalPage",pageBean.getTotalPage());
        data.put("currentPage",pageBean.getCurrentPage());//默认0就是第一页
        data.put("blogList",result);
        return new BaseDto(ResponseStatus.SUCCESS,data);
    }
    public BaseDto deleteBlog(Long id,Long bloggerId){
        log.info("delete blog ======== blogId::::{},bloggerId::::{}",id,bloggerId);
        Blog blog = blogDao.getBlogById(id);
        if(bloggerId.equals(blog.getBloggerId())){
            blog.setDelFlag(1);
            blogDao.updateBlog(blog);
            return new BaseDto(ResponseStatus.SUCCESS);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);
    }
    public BaseDto forbiddenComment(Long id,Long bloggerId){
        Blog blog = blogDao.getBlogById(id);
        if(bloggerId.equals(blog.getBloggerId())){
            if(blog.getIsForbiddenComment()==1){
                blog.setIsForbiddenComment(0);
            }else{
                blog.setIsForbiddenComment(1);
            }
            log.info("禁止评论标记::::::::{}",blog.getIsForbiddenComment());
            blogDao.updateBlog(blog);
            return new BaseDto(ResponseStatus.SUCCESS);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);
    }
    public List uploadContentImg(MultipartFile[] files){
        List<String> imgUrl = new ArrayList<>();
        for(MultipartFile file:files){
            log.info("fileName={}",file.getOriginalFilename());
            String fileName = RandomUtils.getRandomFileName() + "." + FileUtils.getSuffixNameByFileName(file.getOriginalFilename());
            String path = "/spkIMG/sandman/blog/content/" + 7 + "/";//这里的7 到时候换成userId
            String filePath = SftpParam.getPathPrefix() + path;//服务器图片路径
            String linePath = SftpParam.getLinePathPrefix() + path + fileName;//网络图片路径
            log.info("filePath=[{}],linePath=[{}]",filePath,linePath);
            File tempFile = FileUtils.getFileByMultipartFile(file);
            boolean uploadSuccess = FileUtils.upload(filePath,fileName,tempFile);//FileUtils.upload(filePath,fileName,tempFile);//上传服务器
            if(uploadSuccess){
                tempFile.delete();
                imgUrl.add(linePath);
            }
        }
        return imgUrl;
    }
}
