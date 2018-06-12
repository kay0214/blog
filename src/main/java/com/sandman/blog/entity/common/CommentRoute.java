package com.sandman.blog.entity.common;

import com.sandman.blog.entity.user.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentRoute {
    private List<Long> parentIdList = new ArrayList<>();
    private Comment father;
    private List<Comment> son = new ArrayList<>();

    public CommentRoute(Comment father) {
        this.father = father;
        this.parentIdList.add(father.getId());
    }

    public CommentRoute() {
    }

    public List<Long> getParentIdList() {
        return parentIdList;
    }

    public void setParentIdList(List<Long> parentIdList) {
        this.parentIdList = parentIdList;
    }

    public Comment getFather() {
        return father;
    }

    public void setFather(Comment father) {
        this.father = father;
    }

    public List<Comment> getSon() {
        return son;
    }

    public void setSon(List<Comment> son) {
        this.son = son;
    }
    public void addSonList(Comment comment){
        this.parentIdList.add(comment.getId());
        this.son.add(comment);
    }
    public Comment getCompletedFather(){
        this.father.setCommentList(this.son);
        return this.father;
    }

    @Override
    public String toString() {
        return "CommentRoute{" +
                "parentIdList=" + parentIdList +
                ", father=" + father +
                ", son=" + son +
                '}';
    }
}
