package com.ema.pojo;

import java.util.Date;

public class ICThumbUp {
    private Integer uId;

    private Integer icId;

    private Date createTime;

    private Date updateTime;

    public ICThumbUp(Integer uId, Integer icId, Date createTime, Date updateTime) {
        this.uId = uId;
        this.icId = icId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ICThumbUp() {
        super();
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getIcId() {
        return icId;
    }

    public void setIcId(Integer icId) {
        this.icId = icId;
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