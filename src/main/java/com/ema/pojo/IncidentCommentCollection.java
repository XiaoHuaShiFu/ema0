package com.ema.pojo;

import java.util.Date;

public class IncidentCommentCollection {
    private Integer userId;

    private Integer incidentCommentId;

    private Date createTime;

    private Date updateTime;

    public IncidentCommentCollection(Integer userId, Integer incidentCommentId, Date createTime, Date updateTime) {
        this.userId = userId;
        this.incidentCommentId = incidentCommentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public IncidentCommentCollection() {
        super();
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