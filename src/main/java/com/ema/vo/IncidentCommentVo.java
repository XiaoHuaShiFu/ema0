package com.ema.vo;

import java.util.List;

public class IncidentCommentVo {
    private Integer id;

    private UserVo userVo;

    private Integer incidentId;

    private Boolean anon;

    private String comment;

    private Integer comments;

    private Integer collections;

    private Integer collection;

    private Integer thumbUps;

    private Integer thumbUp;

    private String commentTime;

    private String mainImageUrl;

    private List<IncidentScndCommentVo> incidentScndCommentVoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
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
        this.comment = comment;
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

    public Integer getCollection() {
        return collection;
    }

    public void setCollection(Integer collection) {
        this.collection = collection;
    }

    public Integer getThumbUps() {
        return thumbUps;
    }

    public void setThumbUps(Integer thumbUps) {
        this.thumbUps = thumbUps;
    }

    public Integer getThumbUp() {
        return thumbUp;
    }

    public void setThumbUp(Integer thumbUp) {
        this.thumbUp = thumbUp;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public List<IncidentScndCommentVo> getIncidentScndCommentVoList() {
        return incidentScndCommentVoList;
    }

    public void setIncidentScndCommentVoList(List<IncidentScndCommentVo> incidentScndCommentVoList) {
        this.incidentScndCommentVoList = incidentScndCommentVoList;
    }

}