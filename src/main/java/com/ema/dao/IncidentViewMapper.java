package com.ema.dao;

import com.ema.pojo.IncidentView;

public interface IncidentViewMapper {
    int insert(IncidentView record);

    int insertSelective(IncidentView record);
}