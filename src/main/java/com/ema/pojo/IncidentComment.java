package com.ema.pojo;

import java.util.Date;

public class IncidentComment {
    private Integer id;

    private Integer userId;

    private Integer incidentId;

    private Boolean anon;

    private String comment;

    private Integer comments;

    private Integer collections;

    private Integer thumbUps;

    private Date commentTime;

    private String mainImageUrl;

    private Date createTime;

    private Date updateTime;

    public IncidentComment(Integer id, Integer userId, Integer incidentId, Boolean anon, String comment, Integer comments, Integer collections, Integer thumbUps, Date commentTime, String mainImageUrl, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.incidentId = incidentId;
        this.anon = anon;
        this.comment = comment;
        this.comments = comments;
        this.collections = collections;
        this.thumbUps = thumbUps;
        this.commentTime = commentTime;
        this.mainImageUrl = mainImageUrl;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public IncidentComment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public Boolean getAnon() {
        return anon;
    }

    public void setAnon(Boolean anon) {
        this.anon = anon;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getCollections() {
        return collections;
    }

    public void setCollections(Integer collections) {
        this.collections = collections;
    }

    public Integer getThumbUps() {
        return thumbUps;
    }

    public void setThumbUps(Integer thumbUps) {
        this.thumbUps = thumbUps;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl == null ? null : mainImageUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}