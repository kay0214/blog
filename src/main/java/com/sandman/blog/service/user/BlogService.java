package com.sandman.blog.service.user;

import com.github.pagehelper.PageHelper;
import com.sandman.blog.dao.mysql.user.BlogDao;
import com.sandman.blog.dao.mysql.user.BloggerDao;
import com.sandman.blog.dao.mysql.user.CategoryDao;
import com.sandman.blog.dao.mysql.user.CommentDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.common.SftpParam;
import com.sandman.blog.entity.common.SortParam;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.entity.user.Blogger;
import com.sandman.blog.entity.user.Category;
import com.sandman.blog.entity.user.Comment;
import com.sandman.blog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BloggerService bloggerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    public BaseDto getBlogById(Long id){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        Blog blog = blogDao.getBlogById(id);
        if(bloggerId != null && !bloggerId.equals(blog.getBloggerId())){//登录用户且不是自己点击的，才算阅读量+1
            blog.setClickCount(blog.getClickCount() + 1);//阅读数+1
            blogDao.updateBlog(blog);
        }
        return new BaseDto(ResponseStatus.SUCCESS,blog);
    }
    public BaseDto getAllBlog(Integer pageNumber, Integer size,String sortType,String order){
        String keyWord = "";
        log.info("pageNumber======={},size========{},keyword========{}",pageNumber,size,keyWord);
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        order = (order==null || "".equals(order))?"createTime":order;
        String orderBy = order + " " + sortType;//默认按照id降序排序
        Integer totalRow = blogDao.getAllBlog(keyWord).size();//查询出数据条数
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
        List<Blog> blogList = blogDao.getAllBlog(keyWord);//查询出列表（已经分页）
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
    public BaseDto findByKeyWord(Integer pageNumber, Integer size,String sortType,String order,String keyWord){
        log.info("pageNumber======={},size========{},keyword========{}",pageNumber,size,keyWord);
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        order = (order==null || "".equals(order))?"createTime":order;
        String orderBy = order + " " + sortType;//默认按照id降序排序

        Integer totalRow = blogDao.getAllBlog(keyWord).size();//查询出数据条数
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
        List<Blog> blogList = blogDao.getAllBlog(keyWord);//查询出列表（已经分页）
        PageBean<Blog> pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
        pageBean.setItems(blogList);
        List<Blog> result = pageBean.getItems();

        if(result.size()==0){
            keyWord = null;
            totalRow = blogDao.getAllBlog(keyWord).size();//查询出数据条数
            PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
            blogList = blogDao.getAllBlog(keyWord);//查询出列表（已经分页）
            pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
            pageBean.setItems(blogList);
            result = pageBean.getItems();
        }
        Map data = new HashMap();//最终返回的map

        data.put("totalRow",totalRow);
        data.put("totalPage",pageBean.getTotalPage());
        data.put("currentPage",pageBean.getCurrentPage());//默认0就是第一页
        data.put("blogList",result);
        return new BaseDto(ResponseStatus.SUCCESS,data);
    }
    /**
     * 根据bloggerId查询博客。0：全部博客；1：仅发表博客；2：私密博客；3：博客草稿
     * */
    public BaseDto findByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId,Integer publicBlogs){
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        order = (order==null || "".equals(order))?"createTime":order;
        String orderBy = order + " " + sortType;//默认按照id降序排序

        Integer totalRow = 0;
        PageBean<Blog> pageBean = null;
        List<Blog> result = new ArrayList<>();
        if(publicBlogs == 0){//查询所有博客
            totalRow = blogDao.findByBloggerId(bloggerId).size();//查询出数据条数
            PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
            List<Blog> blogList = blogDao.findByBloggerId(bloggerId);//查询出列表（已经分页）
            pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
            pageBean.setItems(blogList);
            result = pageBean.getItems();
        }else if(publicBlogs == 1){//仅发表的博客
            totalRow = blogDao.findPublicByBloggerId(bloggerId).size();//查询出数据条数
            PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
            List<Blog> blogList = blogDao.findPublicByBloggerId(bloggerId);//查询出列表（已经分页）
            pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
            pageBean.setItems(blogList);
            result = pageBean.getItems();
        }else if(publicBlogs == 2){//私密博客
            totalRow = blogDao.findOnlyMeReadByBloggerId(bloggerId).size();//查询出数据条数
            PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
            List<Blog> blogList = blogDao.findOnlyMeReadByBloggerId(bloggerId);//查询出列表（已经分页）
            pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
            pageBean.setItems(blogList);
            result = pageBean.getItems();
        }else if(publicBlogs == 3){//博客草稿
            totalRow = blogDao.findDraftByBloggerId(bloggerId).size();//查询出数据条数
            PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);
            List<Blog> blogList = blogDao.findDraftByBloggerId(bloggerId);//查询出列表（已经分页）
            pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码
            pageBean.setItems(blogList);
            result = pageBean.getItems();
        }else{//请求错误
            return new BaseDto(ResponseStatus.SUCCESS);
        }

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
        if(blog != null && bloggerId.equals(blog.getBloggerId())){
            //删除一篇博客
            blog.setDelFlag(1);
            blogDao.updateBlog(blog);
            //blogger的博客数量 - 1
            if(blog.getBlogType() == 0){//原创
                bloggerService.reduceOriginalBlogCount(bloggerId);//原创数量-1
            }else if(blog.getBlogType() == 1){//转载
                bloggerService.reduceTransferBlogCount(bloggerId);//转载数量-1
            }else{
                return new BaseDto(ResponseStatus.HAVE_NO_DATA);
            }
            //删除blogId的评论
            Integer rowCount = commentService.deleteCommentByBlogId(blog.getId());
            bloggerService.reduceCommentCount(bloggerId,rowCount);
            //对应的个人分类 - 1
            categoryService.reduceCountByCategoryId(blog.getCategoryId());
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
    public BaseDto saveBlog(Blog blog){
        log.info(blog.toString());
        Category category = categoryDao.getCategoryById(blog.getCategoryId());//根据用户自定义类型id查询自定义类型
        if(blog.getId() != null){ //id不为空就是修改
            Blog savedBlog = blogDao.getBlogById(blog.getId());
            if(savedBlog!=null){//能查询出来博客
                if(!blog.getBloggerId().equals(savedBlog.getBloggerId())){//如果修改人不是博主本人，返回无权修改
                    return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_UPDATE);
                }
                savedBlog.setTitle(blog.getTitle());//更新标题
                if(blog.getContentNoTag().length()>100){
                    savedBlog.setSummary(blog.getContentNoTag().substring(0,100));//更新摘要，截取0-100的不带tag的内容
                }else{
                    savedBlog.setSummary(blog.getContentNoTag());//更新摘要，截取0-100的不带tag的内容
                }

                savedBlog.setContent(blog.getContent());//更新带tag的内容
                savedBlog.setContentNoTag(blog.getContentNoTag());//更新不带tag的内容
                savedBlog.setIsDraft(blog.getIsDraft());//更新是否草稿
                savedBlog.setBlogType(blog.getBlogType());//更新博客类型，原创or转载
                savedBlog.setOnlyMeRead(blog.getOnlyMeRead());//更新博客私密性
                savedBlog.setCategoryId(blog.getCategoryId());//更新博客的用户自定义类型id
                savedBlog.setCategoryName(category.getCategoryName());//更新用户自定义类型名
                savedBlog.setKeyWord(blog.getKeyWord().trim());//更新博客关键词
                savedBlog.setUpdateBy(blog.getBloggerId());
                savedBlog.setUpdateTime(ZonedDateTime.now());
                blogDao.updateBlog(savedBlog);//更新到数据库
                return new BaseDto(ResponseStatus.SUCCESS);
            }
            return new BaseDto(ResponseStatus.HAVE_NO_DATA);
        }else{//id为空就是新增
            if(blog.getContentNoTag().length()>100){
                blog.setSummary(blog.getContentNoTag().substring(0,100));//设置摘要，截取0-100的不带tag的内容
            }else{
                blog.setSummary(blog.getContentNoTag());//设置摘要，截取0-100的不带tag的内容
            }
            blog.setClickCount(0);
            blog.setReplayCount(0);
            blog.setIsTop(0);
            blog.setIsForbiddenComment(0);
            blog.setCategoryName(category.getCategoryName());
            blog.setCreateBy(blog.getBloggerId());
            blog.setCreateTime(ZonedDateTime.now());
            blog.setUpdateBy(blog.getBloggerId());
            blog.setUpdateTime(ZonedDateTime.now());
            blog.setDelFlag(0);
            blogDao.createBlog(blog);//创建到数据库
            categoryService.addCountByCategoryId(blog.getCategoryId());//对应的分类博客数量 + 1
            if(blog.getBlogType() == 1){
                bloggerService.addTransferBlogCount(blog.getBloggerId());//转载博客数 + 1
            }else{
                bloggerService.addOriginalBlogCount(blog.getBloggerId());//原创博客数 + 1
            }
            return new BaseDto(ResponseStatus.SUCCESS);
        }
    }
    public List uploadContentImg(MultipartFile[] files){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        List<String> imgUrl = new ArrayList<>();
        for(MultipartFile file:files){
            String fileName = RandomUtils.getRandomFileName() + "." + FileUtils.getSuffixNameByFileName(file.getOriginalFilename());
            String path = "/content/" + bloggerId + "/";//这里的7 到时候换成userId
            String filePath = SftpParam.getPathPrefix() + path;//服务器图片路径
            String linePath = SftpParam.getLinePathPrefix() + path + fileName;//网络图片路径
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
