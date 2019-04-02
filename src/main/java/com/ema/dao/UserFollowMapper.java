package com.ema.dao;

import com.ema.pojo.UserFollow;

public interface UserFollowMapper {
    int insert(UserFollow record);

    int insertSelective(UserFollow record);
}