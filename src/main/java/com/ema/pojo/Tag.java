package com.ema.pojo;

import java.util.Date;

public class Tag {
    private Integer id;

    private String name;

    private Integer num;

    private Date createTime;

    private Date updateTime;

    public Tag(Integer id, String name, Integer num, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Tag() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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