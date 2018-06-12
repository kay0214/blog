package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.service.user.BloggerService;
import com.sandman.blog.utils.ShiroSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/blog/v1")
public class BloggerController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BloggerService bloggerService;

    @ApiOperation(value = "根据id获取博主信息")
    @GetMapping("/blogger/getBloggerById")
    public BaseDto getBloggerById(Long id){
        return bloggerService.getBloggerById(id);
    }
    @ApiOperation(value = "博主修改头像")
    @PostMapping("/blogger/modifyAvatar")
    public BaseDto modifyAvatar(MultipartFile file){
        log.info("用户修改头像:::::::::::::::::::");
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        log.info("博主id::::::::::::::{}",bloggerId);
        if(bloggerId != null){
            return bloggerService.modifyAvatar(file,bloggerId);
        }
        return new BaseDto(ResponseStatus.USER_NOT_LOGIN);
    }
}
