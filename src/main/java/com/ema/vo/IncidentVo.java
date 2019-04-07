package com.ema.vo;

import java.util.Date;

public class IncidentVo {
    private Integer id;

    private Integer userId;

    private Boolean anon;

    private Integer views;

    private Integer attentions;

    private Integer comments;

    private String tags;

    private String title;

    private Date occurTime;

    private String address;

    private String addressName;

    private String description;

    private String mainImageUrl;

    private String mainVideoUrl;

    private Float latitude;

    private Float longitude;

    private Date createTime;

    private Date updateTime;

    public IncidentVo(Integer id, Integer userId, Boolean anon, Integer views, Integer attentions, Integer comments, String tags, String title, Date occurTime, String address, String addressName, String description, String mainImageUrl, String mainVideoUrl, Float latitude, Float longitude, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.anon = anon;
        this.views = views;
        this.attentions = attentions;
        this.comments = comments;
        this.tags = tags;
        this.title = title;
        this.occurTime = occurTime;
        this.address = address;
        this.addressName = addressName;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.mainVideoUrl = mainVideoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public IncidentVo() {
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

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl == null ? null : mainImageUrl.trim();
    }

    public String getMainVideoUrl() {
        return mainVideoUrl;
    }

    public void setMainVideoUrl(String mainVideoUrl) {
        this.mainVideoUrl = mainVideoUrl == null ? null : mainVideoUrl.trim();
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