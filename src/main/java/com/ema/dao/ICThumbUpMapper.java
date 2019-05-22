package com.ema.dao;

import com.ema.pojo.ICThumbUp;
import org.apache.ibatis.annotations.Param;

public interface ICThumbUpMapper {
    int insert(ICThumbUp record);

    int insertSelective(ICThumbUp record);

    int deleteByUserIdAndICId(@Param("icId") Integer icId, @Param("userId") Integer userId);

    int selectCountByIncidentCommentIdAndUserId(@Param("icId") Integer icId, @Param("userId") Integer userId);
}