package com.sandman.blog.controller;

import com.alibaba.fastjson.JSON;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.service.user.BlogService;
import com.sandman.blog.utils.ShiroSecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog/v1")
public class BlogController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "根据id获取单个BLOG")
    @GetMapping("/blog/getBlogById")
    public BaseDto getBlogById(Long id){
        if(id!=null){
            return blogService.getBlogById(id);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    @ApiOperation(value = "分页获取全部博文,首页用。已废弃，合并到关键词搜索中")
    @GetMapping("/blog/getAllBlog")
    public BaseDto getAllBlog(Integer pageNumber, Integer size,String sortType,String order){
        return blogService.getAllBlog(pageNumber, size, sortType, order);
    }
    @ApiOperation(value = "根据关键词模糊搜索")
    @GetMapping("/blog/findByKeyWord")
    public BaseDto findByKeyWord(Integer pageNumber, Integer size,String sortType,String order,String keyWord){
        return blogService.findByKeyWord(pageNumber, size,sortType,order, keyWord);
    }
    @ApiOperation(value = "查询某位博主的所有博客")
    @GetMapping("/blog/findAllByBloggerId")
    public BaseDto findAllByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        return blogService.findByBloggerId(pageNumber, size, sortType, order, bloggerId,false);//非公开博客也一起查询
    }
    @ApiOperation(value = "查询某位博主的所有公开博客")
    @GetMapping("/blog/findByBloggerId")
    public BaseDto findByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        return blogService.findByBloggerId(pageNumber, size, sortType, order, bloggerId,true);
    }
    @ApiOperation(value = "根据id删除博客（假删）")
    @GetMapping("/blog/deleteBlog")
    public BaseDto deleteBlog(Long id){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(bloggerId!=null){
            return blogService.deleteBlog(id,bloggerId);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);
    }
    @ApiOperation(value = "将博客设置为禁止评论状态")
    @GetMapping("/blog/forbiddenComment")
    public BaseDto forbiddenComment(Long id){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(bloggerId!=null){
            return blogService.forbiddenComment(id,bloggerId);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_UPDATE);
    }
    @ApiOperation(value = "保存博客")
    @PostMapping("/blog/saveBlog")
    public BaseDto saveBlog(@RequestBody Blog blog){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        log.info("保存博客的博主id:::::::::::::::{}",bloggerId);
        blog.setBloggerId(bloggerId);
        log.debug(blog.toString());
        if(bloggerId!=null){
            return blogService.saveBlog(blog);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_UPDATE);
    }
    @ApiOperation(value = "上传图片到服务器")
    @PostMapping("/blog/uploadContentImg")
    public String uploadContentImg(MultipartFile[] files){
        log.info("enter upload");
        List<String> list = blogService.uploadContentImg(files);
        Map map = new HashMap();
        if(list.size()>0){
            map.put("errno",0);
        }else{
            map.put("errno",1);
        }
        map.put("data",list);
        return JSON.toJSONString(map);
    }
}
