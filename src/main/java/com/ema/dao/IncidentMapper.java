package com.ema.dao;

import com.ema.pojo.Incident;

public interface IncidentMapper {
    int insert(Incident record);

    int insertSelective(Incident record);

    int deleteByPrimaryKey(Integer id);

    Incident selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Incident record);

    int updateByPrimaryKey(Incident record);
}