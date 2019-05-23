package com.ema.pojo;

import java.util.Date;

public class IncidentScndComment {
    private Integer id;

    private Integer userId;

    private Integer incidentCommentId;

    private Integer parScndCommentId;

    private String comment;

    private Integer thumbUps;

    private Date commentTime;

    private Date createTime;

    private Date updateTime;

    public IncidentScndComment(Integer id, Integer userId, Integer incidentCommentId, Integer parScndCommentId, String comment, Integer thumbUps, Date commentTime, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.incidentCommentId = incidentCommentId;
        this.parScndCommentId = parScndCommentId;
        this.comment = comment;
        this.thumbUps = thumbUps;
        this.commentTime = commentTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public IncidentScndComment() {
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

    public Integer getIncidentCommentId() {
        return incidentCommentId;
    }

    public void setIncidentCommentId(Integer incidentCommentId) {
        this.incidentCommentId = incidentCommentId;
    }

    public Integer getParScndCommentId() {
        return parScndCommentId;
    }

    public void setParScndCommentId(Integer parScndCommentId) {
        this.parScndCommentId = parScndCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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