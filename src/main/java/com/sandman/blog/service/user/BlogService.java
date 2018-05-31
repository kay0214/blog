package com.sandman.blog.service.user;

import com.github.pagehelper.PageHelper;
import com.sandman.blog.dao.mysql.user.BlogDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.common.SftpParam;
import com.sandman.blog.entity.common.SortParam;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.utils.FileUtils;
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
import java.util.List;

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
