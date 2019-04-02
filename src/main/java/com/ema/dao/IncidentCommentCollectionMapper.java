package com.ema.dao;

import com.ema.pojo.IncidentCommentCollection;

public interface IncidentCommentCollectionMapper {
    int insert(IncidentCommentCollection record);

    int insertSelective(IncidentCommentCollection record);
}