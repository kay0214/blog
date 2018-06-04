package com.sandman.blog.service.user;

import com.sandman.blog.dao.mysql.user.CommentDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommentDao commentDao;
    public List<Comment> getMyComment(Long bloggerId){
        return commentDao.getMyComment(bloggerId);
    }
    public List<Comment> getCommentMe(Long bloggerId){
        return commentDao.getCommentMe(bloggerId);
    }
    public BaseDto deleteComment(Long id,Long bloggerId){
        log.info("deleteComment=========id:::{},bloggerId:::{}",id,bloggerId);
        Comment comment = commentDao.findById(id);
        if(bloggerId.equals(comment.getBlog().getBloggerId()) || bloggerId.equals(comment.getBloggerId())){//如果是评论我的，或者是我评论的。那么就可以删除
            commentDao.deleteComment(id);
            return new BaseDto(ResponseStatus.SUCCESS);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);

    }
}
