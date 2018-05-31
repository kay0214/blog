package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.service.user.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @ApiOperation(value = "根据id获取单个category")
    @GetMapping("/category/getCategoryById")
    public BaseDto getCategoryById(Long id){
        if(id!=null){
            return categoryService.getCategoryById(id);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
}
