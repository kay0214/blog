package com.sandman.blog.service.user;

import com.github.pagehelper.PageHelper;
import com.sandman.blog.dao.mysql.user.CategoryDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.entity.user.Category;
import com.sandman.blog.utils.PageBean;
import com.sandman.blog.utils.ShiroSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryDao categoryDao;
    public BaseDto getCategoryById(Long id){
        Category category = categoryDao.getCategoryById(id);
        if(category!=null){
            return new BaseDto(ResponseStatus.SUCCESS,category);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    public BaseDto getCategoryListByBloggerId(Integer pageNumber, Integer size,String sortType,String order,Long bloggerId){
        log.info("pageNumber======={},size========{},bloggerId========{}",pageNumber,size,bloggerId);
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        order = (order==null || "".equals(order))?"createTime":order;
        String orderBy = order + " " + sortType;//默认按照id降序排序

        Integer totalRow = categoryDao.getCategoryListByBloggerId(bloggerId).size();//查询出数据条数
        log.info("totalRow:::::{}",totalRow);
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);

        List<Category> categoryList = categoryDao.getCategoryListByBloggerId(bloggerId);//查询出列表（已经分页）

        PageBean<Category> pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码

        pageBean.setItems(categoryList);
        List<Category> result = pageBean.getItems();

        Map data = new HashMap();//最终返回的map

        data.put("totalRow",totalRow);
        data.put("totalPage",pageBean.getTotalPage());
        data.put("currentPage",pageBean.getCurrentPage());//默认0就是第一页
        data.put("categoryList",result);
        data.forEach((key,value) -> {
            System.out.println(key + "::::::" + value);
        });
        return new BaseDto(ResponseStatus.SUCCESS,data);
    }
    public BaseDto getAllCategoryByBloggerId(Long bloggerId){
        List<Category> categoryList = categoryDao.getCategoryListByBloggerId(bloggerId);
        return new BaseDto(ResponseStatus.SUCCESS,categoryList);
    }
    public BaseDto createCategory(String categoryName){
        log.info("categoryName============={}",categoryName);
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(bloggerId == null){
            return new BaseDto(ResponseStatus.USER_NOT_LOGIN);
        }
        Category category = new Category();
        category.setBloggerId(bloggerId);
        category.setCategoryName(categoryName);
        category.setBlogCount(0);
        Integer maxOrderNo = categoryDao.findMaxOrder(bloggerId);
        log.info("maxOrderNo====={}",maxOrderNo);
        maxOrderNo = (maxOrderNo == null)?1:(maxOrderNo + 1);
        category.setOrderNo(maxOrderNo);
        category.setCreateTime(ZonedDateTime.now());
        category.setUpdateTime(ZonedDateTime.now());
        category.setDelFlag(0);
        boolean success = categoryDao.createCategory(category);
        log.info("insert category ============ {}",success);
        return new BaseDto(ResponseStatus.SUCCESS);
    }
    public void addCountByCategoryId(Long id){
        Category category = categoryDao.getCategoryById(id);
        category.setBlogCount(category.getBlogCount() + 1);
        categoryDao.updateCategory(category);
    }
    public void reduceCountByCategoryId(Long id){
        Category category = categoryDao.getCategoryById(id);
        category.setBlogCount(category.getBlogCount() - 1);
        categoryDao.updateCategory(category);
    }
    public BaseDto deleteCategory(Long id,Long bloggerId){
        Category category = categoryDao.getCategoryById(id);
        if(category!=null){
            if(bloggerId.equals(category.getBloggerId())){
                category.setDelFlag(1);
                categoryDao.updateCategory(category);
                return new BaseDto(ResponseStatus.SUCCESS);
            }
            return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);

        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
    public BaseDto updateCategory(Long id,String categoryName,Long bloggerId){
        Category category = categoryDao.getCategoryById(id);
        if(category!=null){
            if(bloggerId.equals(category.getBloggerId())){
                category.setCategoryName(categoryName);
                categoryDao.updateCategory(category);
                return new BaseDto(ResponseStatus.SUCCESS);
            }
            return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_UPDATE);
        }
        return new BaseDto(ResponseStatus.HAVE_NO_DATA);
    }
}
