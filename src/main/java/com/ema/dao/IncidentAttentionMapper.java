package com.ema.dao;

import com.ema.pojo.IncidentAttention;

public interface IncidentAttentionMapper {
    int insert(IncidentAttention record);

    int insertSelective(IncidentAttention record);
}