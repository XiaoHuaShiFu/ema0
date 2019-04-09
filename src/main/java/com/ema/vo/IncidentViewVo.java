package com.ema.vo;

public class IncidentViewVo {
    private Integer incidentId;

    private String incidentTitle;

    private Integer views;

    private Integer attentions;

    private Integer comments;


    public Integer getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public String getIncidentTitle() {
        return incidentTitle;
    }

    public void setIncidentTitle(String incidentTitle) {
        this.incidentTitle = incidentTitle;
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
}