package com.ema.dao;

import com.ema.pojo.TagIncidentMap;

public interface TagIncidentMapMapper {
    int insert(TagIncidentMap record);

    int insertSelective(TagIncidentMap record);
}