package com.ema.dao;

import com.ema.pojo.Tag;

import java.util.List;

public interface TagMapper {
    int insert(Tag record);

    int insertSelective(Tag record);

    List<String> selectTagListById(int[] tags);

    List<Tag> selectTagListByIncidentId(Integer incidentId);

    int insertTagIfNotExist(Tag tag);

    int selectCountByName(String name);

    List<Tag> selectTagList();

    int incrNumByTagIds(int[] tags);
}