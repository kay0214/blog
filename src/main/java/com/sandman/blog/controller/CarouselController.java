package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.service.user.CarouselService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/v1")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "首页轮播图信息")
    @GetMapping("/carousel/getAllCarousel")
    public BaseDto getAllCarousel(){
        return carouselService.getAllCarousel();
    }
}
