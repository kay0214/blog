package com.sandman.blog.service.user;

import com.sandman.blog.dao.mysql.user.CategoryDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Category;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    public BaseDto getCategoryById(Long id){
        Category category = categoryDao.getCategoryById(id);
        if(category!=null){
            return new BaseDto(ResponseStatus.SUCCESS,category);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
}
