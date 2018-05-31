package com.sandman.blog.entity.user;

import java.time.ZonedDateTime;

public class Blog {
    private Long id;
    private Long bloggerId;//博主id
    private String title;
    private String summary;//摘要，显示前100字符
    private String content;//带网页标签的内容
    private String contentNoTag;//不带网页标签的内容
    private Integer clickCount;//点击次数，阅读数
    private Integer replayCount;//评论次数
    private Integer isTop;//是否置顶，1置顶；0不置顶（每个用户只能有一个置顶博客）
    private Integer isDraft;//是否是草稿，1草稿；0正式发布文章
    private Integer blogType;//博客类型,0原创博客；1转载博客
    private Integer onlyMeRead;//私密文章
    private Long categoryId;//用户自定义分类id，可为空
    private String categoryName;//用户自定义分类name，用于在博客详细内容里显示
    private String keyWord;
    private Long createBy;//创建人
    private ZonedDateTime createTime;//创建时间
    private Long updateBy;//更新人
    private ZonedDateTime updateTime;//更新时间
    private Integer delFlag;//删除标记
    private Blogger blogger;//博主详细信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBloggerId() {
        return bloggerId;
    }

    public void setBloggerId(Long bloggerId) {
        this.bloggerId = bloggerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentNoTag() {
        return contentNoTag;
    }

    public void setContentNoTag(String contentNoTag) {
        this.contentNoTag = contentNoTag;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(Integer replayCount) {
        this.replayCount = replayCount;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    public Integer getBlogType() {
        return blogType;
    }

    public void setBlogType(Integer blogType) {
        this.blogType = blogType;
    }

    public Integer getOnlyMeRead() {
        return onlyMeRead;
    }

    public void setOnlyMeRead(Integer onlyMeRead) {
        this.onlyMeRead = onlyMeRead;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", bloggerId=" + bloggerId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", contentNoTag='" + contentNoTag + '\'' +
                ", clickCount=" + clickCount +
                ", replayCount=" + replayCount +
                ", isTop=" + isTop +
                ", isDraft=" + isDraft +
                ", blogType=" + blogType +
                ", onlyMeRead=" + onlyMeRead +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                ", blogger=" + blogger +
                '}';
    }
}
