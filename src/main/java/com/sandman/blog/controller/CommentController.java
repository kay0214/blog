package com.sandman.blog.controller;

import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Comment;
import com.sandman.blog.service.user.CommentService;
import com.sandman.blog.utils.ShiroSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/blog/v1")
public class CommentController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommentService commentService;
    @ApiOperation(value = "获取用户的评论 => 我评论的")
    @GetMapping("/comment/getMyComment")
    public BaseDto getMyComment(Integer pageNumber, Integer size,Long bloggerId){
        //TODO:分页显示
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        List<Comment> commentList = new ArrayList<>();
        if(bloggerId!=null){
            log.info("bloggerId={}",bloggerId);
            return commentService.getMyComment(pageNumber, size, bloggerId);//获取 "我评论的" 所有评论
        }
        log.info("我评论别人====={}",commentList);
        return new BaseDto(ResponseStatus.SUCCESS,commentList);
    }
    @ApiOperation(value = "获取用户的评论 => 评论我的")
    @GetMapping("/comment/getCommentMe")
    public BaseDto getCommentMe(Integer pageNumber, Integer size,Long bloggerId){
        //TODO:分页显示
        bloggerId = (bloggerId == null)?ShiroSecurityUtils.getCurrentUserId():bloggerId;
        List<Comment> commentList = new ArrayList<>();
        if(bloggerId!=null){
            log.info("bloggerId={}",bloggerId);
            return commentService.getCommentMe(pageNumber, size, bloggerId);
        }
        log.info("别人评论我====={}",commentList);
        return new BaseDto(ResponseStatus.SUCCESS,commentList);
    }
    @ApiOperation(value = "根据comment的id假删")
    @GetMapping("/comment/deleteComment")
    public BaseDto deleteComment(Long id){
        // 要考虑到  是不是自己评论的，或者是不是评论自己博客的
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(bloggerId!=null){
            log.info("bloggerId={}",bloggerId);
            return commentService.deleteComment(id,bloggerId);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);
    }
    @ApiOperation(value = "根据blogId查询评论和回复")
    @GetMapping("/comment/getCommentByBlogId")
    public BaseDto getCommentByBlogId(Long blogId){
        List<Comment> commentList = new ArrayList<>();
        if(blogId!=null){
            log.info("blogId={}",blogId);
            commentList = commentService.getCommentByBlogId(blogId);
        }
        log.info("查询评论和回复====={}",commentList);
        return new BaseDto(ResponseStatus.SUCCESS,commentList);
    }
    @ApiOperation(value = "评论博客或者回复评论")
    @PostMapping("/comment/commentBlog")
    public BaseDto commentBlog(Comment comment){
        Long bloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(bloggerId != null){
            comment.setBloggerId(bloggerId);
            return commentService.createComment(comment);
        }
        return new BaseDto(ResponseStatus.USER_NOT_LOGIN);
    }
}
