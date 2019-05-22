package com.ema.dao;

import com.ema.pojo.IncidentCommentCollection;
import org.apache.ibatis.annotations.Param;

public interface IncidentCommentCollectionMapper {
    int insert(IncidentCommentCollection record);

    int insertSelective(IncidentCommentCollection record);

    int deleteByUserIdAndCommentId(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    int selectCountByIncidentCommentIdAndUserId(@Param("incidentCommentId") Integer incidentCommentId, @Param("userId") Integer userId);
}