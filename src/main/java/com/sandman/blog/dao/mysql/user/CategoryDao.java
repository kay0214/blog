package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.user.Category;
import org.springframework.stereotype.Repository;

/**
 * Created by sunpeikai on 2018/5/4.
 */
@Repository
public interface CategoryDao {
    public Category getCategoryById(Long id);
}
