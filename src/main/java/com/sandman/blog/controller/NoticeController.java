package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.service.user.NoticeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/v1")
public class NoticeController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private NoticeService noticeService;
    @ApiOperation(value = "获取所有公告")
    @GetMapping("/notice/getAllNotice")
    public BaseDto getAllNotice(){
        return noticeService.getAllNotice();
    }
}
