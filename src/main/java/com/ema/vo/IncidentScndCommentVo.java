package com.ema.vo;

public class IncidentScndCommentVo {
    private Integer id;

    private UserVo userVo;

    private UserVo parScndCommentUserVo;

    private Integer incidentCommentId;

    private String comment;

    private Integer thumbUps;

    private Boolean isThumbUp; //是否已经点赞

    private String commentTime;

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

    public UserVo getParScndCommentUserVo() {
        return parScndCommentUserVo;
    }

    public void setParScndCommentUserVo(UserVo parScndCommentUserVo) {
        this.parScndCommentUserVo = parScndCommentUserVo;
    }

    public Integer getIncidentCommentId() {
        return incidentCommentId;
    }

    public void setIncidentCommentId(Integer incidentCommentId) {
        this.incidentCommentId = incidentCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getThumbUps() {
        return thumbUps;
    }

    public void setThumbUps(Integer thumbUps) {
        this.thumbUps = thumbUps;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public Boolean getThumbUp() {
        return isThumbUp;
    }

    public void setThumbUp(Boolean thumbUp) {
        isThumbUp = thumbUp;
    }
}