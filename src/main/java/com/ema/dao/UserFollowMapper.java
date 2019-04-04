package com.ema.dao;

import com.ema.pojo.UserFollow;

public interface UserFollowMapper {
    int insert(UserFollow record);

    int selectCountByFollowederIdAndFollowerId(UserFollow userFollow);

    int deleteByFollowederIdAndFollowerId(UserFollow userFollow);
}