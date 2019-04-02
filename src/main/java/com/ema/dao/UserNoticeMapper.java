package com.ema.dao;

import com.ema.pojo.UserNotice;

public interface UserNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserNotice record);

    int insertSelective(UserNotice record);

    UserNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserNotice record);

    int updateByPrimaryKey(UserNotice record);
}