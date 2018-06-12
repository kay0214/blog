package com.sandman.blog.entity.user;

import java.time.ZonedDateTime;

public class Blogger {
    private Long id;
    private String userName;
    private String nickName;
    private String sign;//个性签名
    private String proFile;//个人简介
    private String imageUrl;//头像url地址
    private Integer originalBlogCount;
    private Integer transferBlogCount;
    private Integer visitCount;
    private Integer commentCount;
    private Long createBy;//创建人
    private ZonedDateTime createTime;//创建时间
    private Long updateBy;//更新人
    private ZonedDateTime updateTime;//更新时间
    private Integer delFlag;//删除标记

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProFile() {
        return proFile;
    }

    public void setProFile(String proFile) {
        this.proFile = proFile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOriginalBlogCount() {
        return originalBlogCount;
    }

    public void setOriginalBlogCount(Integer originalBlogCount) {
        this.originalBlogCount = originalBlogCount;
    }

    public Integer getTransferBlogCount() {
        return transferBlogCount;
    }

    public void setTransferBlogCount(Integer transferBlogCount) {
        this.transferBlogCount = transferBlogCount;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Blogger{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sign='" + sign + '\'' +
                ", proFile='" + proFile + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", originalBlogCount=" + originalBlogCount +
                ", transferBlogCount=" + transferBlogCount +
                ", visitCount=" + visitCount +
                ", commentCount=" + commentCount +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }
}
