package com.ema.pojo;

import java.util.Date;

public class IncidentView {
    private Integer userId;

    private Integer incidentId;

    private Date createTime;

    private Date updateTime;

    public IncidentView(Integer userId, Integer incidentId, Date createTime, Date updateTime) {
        this.userId = userId;
        this.incidentId = incidentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public IncidentView() {
        super();
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