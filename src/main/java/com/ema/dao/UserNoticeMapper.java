package com.ema.dao;

import com.ema.pojo.UserNotice;

import java.util.List;

public interface UserNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserNotice record);

    int insertSelective(UserNotice record);

    UserNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserNotice record);

    int updateByPrimaryKey(UserNotice record);

    List<String> selectCountByViewAndUserId(UserNotice record);

    int updateByUser_id(UserNotice record);

    int selectViewOrNot(UserNotice record);
}