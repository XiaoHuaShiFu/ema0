package com.ema.dao;

import com.ema.pojo.Incident;

public interface IncidentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Incident record);

    int insertSelective(Incident record);

    Incident selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Incident record);

    int updateByPrimaryKey(Incident record);
}