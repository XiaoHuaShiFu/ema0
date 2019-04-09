package com.ema.dao;

import com.ema.pojo.IncidentComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncidentCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncidentComment record);

    int insertSelective(IncidentComment record);

    IncidentComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncidentComment record);

    int updateByPrimaryKey(IncidentComment record);

    int incrComments(Integer incidentCommentId);

    int decrComments(Integer incidentCommentId);

    int deleteByIdAndUserId(@Param("userId") Integer userId, @Param("id") int id);

    List<IncidentComment> selectByIncidentId(Integer incidentId);

    int incrCollections(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    int decrCollections(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    int incrThumbUps(@Param("id") Integer id, @Param("userId") Integer userId);

    int decrThumbUps(@Param("id") Integer id, @Param("userId") Integer userId);
}