package com.ema.dao;

import com.ema.pojo.IncidentAttention;
import org.apache.ibatis.annotations.Param;

public interface IncidentAttentionMapper {
    int insertSelective(IncidentAttention record);

    int insert(@Param("userId") Integer userId, @Param("incidentId") Integer incidentId);

    int deleteByUserIdAndIncidentId(@Param("incidentId") Integer incidentId, @Param("userId") Integer userId);

    int selectCountByIncidentIdAndUserId(@Param("incidentId") Integer incidentId, @Param("userId") Integer userId);
}