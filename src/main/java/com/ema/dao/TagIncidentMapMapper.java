package com.ema.dao;

import com.ema.pojo.TagIncidentMap;
import org.apache.ibatis.annotations.Param;

public interface TagIncidentMapMapper {
    int insert(TagIncidentMap record);

    int insertSelective(TagIncidentMap record);

    void insertByIncidentIdAndTagId(@Param("id") Integer id, @Param("tags") int[] tags);
}