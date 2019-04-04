package com.ema.dao;

import com.ema.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByOpenid(String openid);

    int selectUserCountByOpenid(String openid);

    String selectOpenidById(Integer id);

    int updateByIdIncrFollowers(Integer id);

    int updateByIdIncrFollowings(Integer id);

    int updateByIdDecrFollowers(Integer id);

    int updateByIdDecrFollowings(Integer id);

    int selectCountById(Integer id);
}