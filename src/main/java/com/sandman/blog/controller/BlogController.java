package com.sandman.blog.controller;

import com.alibaba.fastjson.JSON;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.service.user.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @ApiOperation(value = "分页获取全部博文,首页用")
    @GetMapping("/blog/getAllBlog")
    public BaseDto getAllBlog(Integer pageNumber, Integer size){
        return blogService.getAllBlog(pageNumber,size);
    }
    @ApiOperation(value = "根据关键词模糊搜索")
    @GetMapping("/blog/findByKeyWord")
    public BaseDto findByKeyWord(Integer pageNumber, Integer size,String keyWord){
        return blogService.findByKeyWord(pageNumber, size, keyWord);
    }
    @ApiOperation(value = "查询某位博主的所有博客")
    @GetMapping("/blog/findByBloggerId")
    public BaseDto findByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        return blogService.findByBloggerId(pageNumber, size, sortType, order, bloggerId);
    }
    @ApiOperation(value = "保存博客")
    @PostMapping("/blog/createBlog")
    public BaseDto createBlog(){
        return null;
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
