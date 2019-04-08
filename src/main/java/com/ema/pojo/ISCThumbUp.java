package com.ema.pojo;

import java.util.Date;

public class ISCThumbUp {
    private Integer userId;

    private Integer iscId;


    private Date thumbUpTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIscId() {
        return iscId;
    }

    public void setIscId(Integer iscId) {
        this.iscId = iscId;
    }

    public Date getthumbUpTime() {
        return thumbUpTime;
    }

    public void setthumbUpTime(Date thumbUpTime) {
        this.thumbUpTime = thumbUpTime;
    }
}