package com.ema.dao;

import com.ema.pojo.IncidentScndComment;

public interface IncidentScndCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncidentScndComment record);

    int insertSelective(IncidentScndComment record);

    IncidentScndComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncidentScndComment record);

    int updateByPrimaryKey(IncidentScndComment record);
}