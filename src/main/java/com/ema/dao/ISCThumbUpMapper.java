package com.ema.dao;

import com.ema.pojo.ISCThumbUp;
import org.apache.ibatis.annotations.Param;

public interface ISCThumbUpMapper {
    int insert(@Param("userId") Integer userId, @Param("iscId") Integer iscId);

    int insertSelective(ISCThumbUp record);

    int deleteByUserIdAndISCId(@Param("iscId") Integer iscId, @Param("userId") Integer userId);

    int selectCountByUserIdAndISCId(@Param("userId") Integer userId, @Param("iscId") Integer iscId);
}