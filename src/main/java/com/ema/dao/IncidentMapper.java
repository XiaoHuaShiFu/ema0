package com.ema.dao;

import com.ema.pojo.Incident;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncidentMapper {
    int insert(Incident record);

    int insertSelective(Incident record);

    int deleteByPrimaryKey(Integer id);

    Incident selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Incident record);

    int updateByPrimaryKey(Incident record);

    int incrComments(Integer incidentId);

    int decrComments(Integer incidentId);

    int deleteByIdAndUserId(@Param("userId") Integer userId, @Param("id") Integer id);

    List<Incident> selectListOrderByTime();

    int selectCountByUserIdAndId(@Param("userId") Integer userId, @Param("incidentId") Integer incidentId);

    int incrAttentions(@Param("id") Integer id, @Param("userId") Integer userId);

    int decrAttentions(@Param("id") Integer id, @Param("userId") Integer userId);

    int incrViewsByIncidentId(Integer incidentId);

    String selectTitleById(Integer incidentId);

    int selectUserIdById(Integer incidentId);

    List<Incident> selectByUserId(Integer userId);

    List<Incident> selectByUserId0(Integer userId);

    List<Incident> selectListByTitle(String title);
}