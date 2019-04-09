package com.ema.dao;

import com.ema.pojo.IncidentView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncidentViewMapper {
    int insert(IncidentView record);

    int insertSelective(IncidentView record);

    int selectCountByUserIdAndIncidentId(@Param("userId") Integer userId, @Param("incidentId") Integer incidentId);

    List<IncidentView> selectByUserId(Integer userId);
}