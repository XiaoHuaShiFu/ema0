package com.ema.vo;

import java.util.List;

public class IncidentVo {
    private Integer id;

    private UserVo userVo;

    private Boolean anon;

    private Integer views;

    private Integer attentions;

    private Integer attention;

    private Integer comments;

    private List<TagVo> tagVoList;

    private String title;

    private String occurTime;

    private String address;

    private String addressName;

    private String description;

    private String mainImageUrl;

    private String mainVideoUrl;

    private Float latitude;

    private Float longitude;

    private List<IncidentCommentVo> incidentCommentVoList;

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

    public Boolean getAnon() {
        return anon;
    }

    public void setAnon(Boolean anon) {
        this.anon = anon;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getAttentions() {
        return attentions;
    }

    public void setAttentions(Integer attentions) {
        this.attentions = attentions;
    }

    public Integer getAttention() {
        return attention;
    }

    public void setAttention(Integer attention) {
        this.attention = attention;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public List<TagVo> getTagVoList() {
        return tagVoList;
    }

    public void setTagVoList(List<TagVo> tagVoList) {
        this.tagVoList = tagVoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getMainVideoUrl() {
        return mainVideoUrl;
    }

    public void setMainVideoUrl(String mainVideoUrl) {
        this.mainVideoUrl = mainVideoUrl;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public List<IncidentCommentVo> getIncidentCommentVoList() {
        return incidentCommentVoList;
    }

    public void setIncidentCommentVoList(List<IncidentCommentVo> incidentCommentVoList) {
        this.incidentCommentVoList = incidentCommentVoList;
    }

}