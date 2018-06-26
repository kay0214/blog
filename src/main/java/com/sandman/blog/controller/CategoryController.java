package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.service.user.CategoryService;
import com.sandman.blog.utils.ShiroSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog/v1")
public class CategoryController {
    private final Logger log = LoggerFactory.getLogger(getClass());
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
    @ApiOperation(value = "分页获取用户所有的自定义分类")
    @GetMapping("/category/getCategoryListByBloggerId")
    public BaseDto getCategoryListByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        //TODO 改成分页获取，每页10条
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        if(bloggerId!=null){
            return categoryService.getCategoryListByBloggerId(pageNumber,size,sortType,order,bloggerId);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    @ApiOperation(value = "获取用户所有的自定义分类")
    @GetMapping("/category/getAllCategoryByBloggerId")
    public BaseDto getAllCategoryByBloggerId(Long bloggerId){
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        if(bloggerId!=null){
            return categoryService.getAllCategoryByBloggerId(bloggerId);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    @ApiOperation(value = "用户创建自定义分类")
    @GetMapping("/category/createCategory")
    public BaseDto createCategory(String categoryName){
        log.info("controller ::::::: categoryName======={}",categoryName);
        if(categoryName!=null){
            return categoryService.createCategory(categoryName);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    @ApiOperation(value = "用户删除自定义分类（假删）")
    @GetMapping("/category/deleteCategory")
    public BaseDto deleteCategory(Long id){
        log.info("controller ::::::: id======={}",id);
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(id!=null){
            if(bloggerId!=null){
                return categoryService.deleteCategory(id,bloggerId);
            }
            return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);//无权删除
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    @ApiOperation(value = "用户修改自定义分类名称")
    @GetMapping("/category/updateCategory")
    public BaseDto updateCategory(Long id,String categoryName){
        log.info("controller ::::::: id======={},categoryName======={}",id,categoryName);
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(id!=null){
            if(bloggerId!=null){
                return categoryService.updateCategory(id,categoryName,bloggerId);
            }
            return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_UPDATE);//无权修改
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
}
