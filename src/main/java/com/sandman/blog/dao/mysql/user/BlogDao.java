package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.common.SortParam;
import com.sandman.blog.entity.user.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunpeikai on 2018/5/4.
 */
@Repository
public interface BlogDao {
    public Blog getBlogById(Long id);
    public List<Blog> getAllBlog(@Param("keyWord")String keyWord);
    public List<Blog> findByBloggerId(@Param("bloggerId") Long bloggerId);
    public List<Blog> findAllByBloggerId(@Param("bloggerId") Long bloggerId);
    public void updateBlog(Blog blog);
    public void createBlog(Blog blog);
}
