package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.user.Blogger;
import org.springframework.stereotype.Repository;

/**
 * Created by sunpeikai on 2018/5/4.
 */
@Repository
public interface BloggerDao {
    public Blogger findById(Long id);
    public void updateBlogger(Blogger blogger);
    public void createBlogger(Blogger blogger);
}
