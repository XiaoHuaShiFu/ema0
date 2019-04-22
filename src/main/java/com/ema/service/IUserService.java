package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.User;
import com.ema.pojo.UserFollow;
import com.ema.pojo.UserNotice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-02-28 11:35
 */
public interface IUserService {

    ServerResponse login(String code);

    ServerResponse updateInformation(User user);

    ServerResponse getInformation(Integer id);


    ServerResponse uploadAvatar(String path, MultipartFile avatar, User user);

    ServerResponse register(String code, User user);

    ServerResponse follow(User user, Integer userFollow);

    ServerResponse NewInform(User user, int pageNum, int pageSize);

    ServerResponse AllInform(User user, int pageNum, int pageSize);

    List<UserNotice> UserNoticeList(User user, int pageNum, int pageSize);

}
