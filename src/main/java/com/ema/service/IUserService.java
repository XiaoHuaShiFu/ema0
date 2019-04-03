package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.User;
import org.springframework.web.multipart.MultipartFile;

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
}
