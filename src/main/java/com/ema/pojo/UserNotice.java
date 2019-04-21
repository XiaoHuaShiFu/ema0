package com.ema.pojo;

import java.util.Date;

public class UserNotice {
    private Integer id;

    private Integer userId;

    private Integer view;

    private Short type;

    private Date noticeTime;

    private String title;

    private String content;

    private Integer incidentId;

    private Integer followerId;

    private Integer commenterId;

    private Integer incidentScndCommentId;

    private Date createTime;

    private Date updateTime;

    public UserNotice(Integer id, Integer userId, Integer view, Short type, Date noticeTime, String title, String content, Integer incidentId, Integer followerId, Integer commenterId, Integer incidentScndCommentId, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.view = view;
        this.type = type;
        this.noticeTime = noticeTime;
        this.title = title;
        this.content = content;
        this.incidentId = incidentId;
        this.followerId = followerId;
        this.commenterId = commenterId;
        this.incidentScndCommentId = incidentScndCommentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserNotice() {
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

    public Integer getView(){return  view;}

    public void setView(Integer view){ this.view = view;}

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(Integer commenterId) {
        this.commenterId = commenterId;
    }

    public Integer getIncidentScndCommentId() {
        return incidentScndCommentId;
    }

    public void setIncidentScndCommentId(Integer incidentScndCommentId) {
        this.incidentScndCommentId = incidentScndCommentId;
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