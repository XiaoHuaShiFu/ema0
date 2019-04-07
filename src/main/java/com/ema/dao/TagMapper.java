package com.ema.dao;

import com.ema.pojo.Tag;

import java.util.List;

public interface TagMapper {
    int insert(Tag record);

    int insertSelective(Tag record);

    List<String> selectTagListById(int[] tags);
}