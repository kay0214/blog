package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.user.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunpeikai on 2018/5/4.
 */
@Repository
public interface CommentDao {
    public List<Comment> getMyComment(Long bloggerId);
    public List<Comment> getCommentMe(Long bloggerId);
    public Comment findById(Long id);
    public void deleteComment(Long id);
/*
    public List<Comment> getCommentByBlogId(Long blogId);
*/
}
