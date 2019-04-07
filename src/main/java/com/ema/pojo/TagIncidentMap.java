package com.ema.pojo;

import java.util.Date;

public class TagIncidentMap {
    private Integer tId;

    private Integer iId;

    private Date createTime;

    private Date updateTime;

    public TagIncidentMap(Integer tId, Integer iId, Date createTime, Date updateTime) {
        this.tId = tId;
        this.iId = iId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public TagIncidentMap() {
        super();
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
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