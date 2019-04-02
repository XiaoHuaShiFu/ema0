package com.ema.pojo;

import java.util.Date;

public class UserFollow {
    private Integer followederId;

    private Integer followerId;

    private Date createTime;

    private Date updateTime;

    public UserFollow(Integer followederId, Integer followerId, Date createTime, Date updateTime) {
        this.followederId = followederId;
        this.followerId = followerId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserFollow() {
        super();
    }

    public Integer getFollowederId() {
        return followederId;
    }

    public void setFollowederId(Integer followederId) {
        this.followederId = followederId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
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