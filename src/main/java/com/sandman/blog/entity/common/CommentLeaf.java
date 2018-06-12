package com.sandman.blog.entity.common;

import com.sandman.blog.entity.user.Comment;

import java.util.List;

public class CommentLeaf {
    private Long fatherId;
    private boolean haveFather;
    private boolean haveChildren;
    private Long commentId;
    private Comment comment;
    public CommentLeaf() {
    }

    public CommentLeaf(Comment comment) {
        this.comment = comment;
        if(comment.getParentId()!=0){
            this.haveFather = true;
        }else{
            this.haveFather = false;
        }
        if(comment.getReplayed() == 1){
            this.haveChildren = true;
        }else{
            this.haveChildren = false;
        }
        this.commentId = comment.getId();
        this.fatherId = comment.getParentId();
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public boolean isHaveFather() {
        return haveFather;
    }

    public void setHaveFather(boolean haveFather) {
        this.haveFather = haveFather;
    }

    public boolean isHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(boolean haveChildren) {
        this.haveChildren = haveChildren;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
