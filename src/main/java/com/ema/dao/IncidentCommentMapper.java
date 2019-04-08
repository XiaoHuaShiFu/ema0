package com.ema.dao;

import com.ema.pojo.IncidentComment;

public interface IncidentCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncidentComment record);

    int insertSelective(IncidentComment record);

    IncidentComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncidentComment record);

    int updateByPrimaryKey(IncidentComment record);

    int incrComments(Integer incidentCommentId);

    int decrComments(Integer incidentCommentId);

}