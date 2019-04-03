package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.UserMapper;
import com.ema.pojo.User;
import com.ema.pojo.WechatBody;
import com.ema.service.IFileService;
import com.ema.service.IUserService;
import com.ema.util.PropertiesUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


/**
 * 描述: UserServiceImpl类
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-02-28 11:35
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IFileService iFileService;

    /**
     * 用户登录
     *
     * 如果状态码为0为登录成功
     * 如果状态码为6表明参数code为空
     * 如果状态码为13表明登录失败，需要让用户再次登录
     * 如果状态码为19用户不存在
     *
     * @param code 微信小程序登录时获取的code
     * @return 如果登录成功返回用户信息
     */
    @Override
    public ServerResponse login(String code) {
        //code不能为空
        if (StringUtils.isBlank(code)) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ARGUMENT_CAN_NOT_BE_BLANK.getCode(), ResponseCode.ARGUMENT_CAN_NOT_BE_BLANK.getDesc());
        }
        String openid = getOpenidByCode(code);
        //如果获取不到openid视为登录失败
        if (openid == null) {
            return ServerResponse.create(
                    ResponseCode.LOGIN_FALSE.getCode(), ResponseCode.LOGIN_FALSE.getDesc());
        }
        User user = userMapper.selectByOpenid(openid);
        //用户不存在
        if (user == null) {
            return ServerResponse.create(
                    ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getDesc());
        }
        //去除敏感信息
        user.setPassword(null);
        user.setOpenid(null);
        //登录成功
        return ServerResponse.createBySuccess("login success", user);
    }

    /**
     * 用户注册
     *
     * 如果状态码为0表明注册成功
     * 如果状态码为1表明注册失败，因为数据库操作失误
     * 如果状态码为17表明注册失败，因为没有openid
     * 如果状态码为18表明用户已经存在
     *
     * @param code code 微信小程序登录时获取的 code
     * @param user 用户注册时附带的信息
     * @return 返回带响应码的提示信息
     */
    public ServerResponse register(String code, User user) {
        String openid = getOpenidByCode(code);
        //如果获取不到openid视为注册失败
        if (openid == null) {
            return ServerResponse.create(
                    ResponseCode.REGISTER_FALSE.getCode(), ResponseCode.REGISTER_FALSE.getDesc());
        }
        int count = userMapper.selectUserCountByOpenid(openid);
        //用户已经存在
        if (count >= 1) {
            return ServerResponse.create(
                    ResponseCode.USER_EXIST.getCode(), ResponseCode.USER_EXIST.getDesc());
        }
        //把openid添加到用户pojo里面
        user.setOpenid(openid);
        count = userMapper.insertSelective(user);
        //数据库插入出错
        if (count < 1) {
            return ServerResponse.createByErrorMessage("register fail");
        }
        //注册成功
        return ServerResponse.createBySuccess("register success");
    }

    /**
     * 更新用户个人信息
     * @param user 用户pojo
     * @return 带用户信息的ServerResponse
     */
    @Override
    public ServerResponse updateInformation(User user) {
        User user0 = new User();
        user0.setId(user.getId());
        user0.setPhone(user.getPhone());
        user0.setEmail(user.getEmail());
        user0.setNickName(user.getNickName());
        user0.setGender(user.getGender());
        user0.setAvatarUrl(user.getAvatarUrl());
        int updateCount = userMapper.updateByPrimaryKeySelective(user0);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("update user info success", getUser(user.getId()));
        }
        return ServerResponse.createByErrorMessage("update user info error");
    }


    /**
     * 获取用户个人信息
     * @param id 用户id
     * @return
     */
    @Override
    public ServerResponse getInformation(Integer id) {
        return ServerResponse.createBySuccess(getUser(id));
    }

    /**
     * 上传用户头像
     *
     * @param path 暂存的文件夹路径
     * @param avatar 用户头像
     * @param user 用户pojo
     * @return 带状态码的响应信息
     */
    public ServerResponse uploadAvatar(String path, MultipartFile avatar, User user) {
        String directory = "img/user/avatar/"; //文件夹路径
        String oldAvatarUrl = user.getAvatarUrl(); //旧头像url
        String avatarName = iFileService.upload(avatar, path, directory, UUID.randomUUID().toString()); //新头像名称
        //上传文件失败
        if (StringUtils.isBlank(avatarName)) {
            return ServerResponse.create(
                    ResponseCode.UPLOAD_FILE_FAIL.getCode(), ResponseCode.UPLOAD_FILE_FAIL.getDesc());
        }
        //拼接头像url
        PropertiesUtil.setProps("scauaie.properties");
        user.setAvatarUrl(PropertiesUtil.getProperty("ftp.server.http.prefix") + directory  + avatarName);
        ServerResponse serverResponse = updateInformation(user);
        //更新用户信息时出错
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        //删除旧头像
        deleteOldAvatar(oldAvatarUrl, directory);
        return ServerResponse.createBySuccess("upload avatar success", user);
    }

    /**
     * 删除用户的旧头像
     *
     * @param oldAvatarUrl 旧头像
     * @param directory 文件夹路径
     */
    private void deleteOldAvatar(String oldAvatarUrl, String directory) {
        int count = iFileService.delete(directory, oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1));
        //删除旧头像失败，可能是因为没有旧头像，或者删除失败
        if (count < 1) {
            logger.info("delete user avatar error");
        }
    }

    /**
     * 发送GET请求微信返回appid,session_key等参数的值
     * @param code 微信小程序登录时获取的 code
     * @return ResponseEntity GET响应的主体，包含openid，session_key，errcode，errmsg
     */
    private ResponseEntity<String> getWecharBody(String code) {
        PropertiesUtil.setProps("wechat.properties");
        String url = PropertiesUtil.getProperty("app.url");
        String appid = PropertiesUtil.getProperty("app.id");
        String secret = PropertiesUtil.getProperty("app.secret");
        String grantType = PropertiesUtil.getProperty("grant.type");
        return restTemplate.getForEntity(
                url + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grantType,
                String.class);
    }

    /**
     * 从ResponseEntity提取openid
     *
     * @param code 微信小程序登录时获取的 code
     * @return 返回openid
     */
    private String getOpenidByCode(String code) {
        ResponseEntity<String> responseEntity = getWecharBody(code);
                WechatBody body = gson.fromJson(responseEntity.getBody(), WechatBody.class);
        return body.getOpenid();
    }

    /**
     * 获取用户信息
     * @param id 用户id
     * @return user 用户pojo类，已去除敏感信息
     */
    private User getUser(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        //去除敏感信息
        user.setPassword(null);
        user.setOpenid(null);
        return user;
    }

}
