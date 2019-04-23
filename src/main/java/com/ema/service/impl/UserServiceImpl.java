package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.UserFollowMapper;
import com.ema.dao.UserMapper;
import com.ema.dao.UserNoticeMapper;
import com.ema.pojo.User;
import com.ema.pojo.UserFollow;
import com.ema.pojo.UserNotice;
import com.ema.pojo.WechatBody;
import com.ema.service.IFileService;
import com.ema.service.IUserService;
import com.ema.util.DateTimeUtil;
import com.ema.util.PropertiesUtil;
import com.ema.vo.UserNoticeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
    private UserFollowMapper userFollowMapper;

    @Autowired
    private IFileService iFileService;

    @Autowired
    private UserNoticeMapper userNoticeMapper;

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
     * 如果返回状态码2表明用户越权操作
     * 如果返回状态码1表明关注失败（可能是被关注者不存在）
     * 如果返回状态码0表明关注成功（或者取消关注成功）
     *
     * @param user
     * @param userFollow
     * @return 带状态码的响应信息
     */
    public ServerResponse follow(User user, UserFollow userFollow) {
        //发出此动作的用户一定是关注者
        //所以此用户的id应该等于关注者id
        //否则返回越权操作错误
        if (user.getId() == null || user.getId().equals(userFollow.getFollowerId())) {
            return ServerResponse.create(
                    ResponseCode.UNAUTHORIZED_OPERATION.getCode(), ResponseCode.UNAUTHORIZED_OPERATION.getDesc());
        }

        //检查被关注者是否存在数据库里
        int rowCount = userMapper.selectCountById(userFollow.getFollowederId());
        //被关注者不存在
        if(rowCount < 1) {
            return ServerResponse.createByErrorMessage("followeder not exist");
        }

//        //查找数据库里是否有关注双方的id对
//        rowCount = userFollowMapper.selectCountByFollowederIdAndFollowerId(userFollow);
//        //小于1表明未关注，进行关注操作
//        // TODO: 2019/4/3 由于对事务不熟悉，这块就先不完善
//        if (rowCount < 1) {
//            // TODO: 2019/4/3 这里应该是通过事务管理确保数据完整性
//            // TODO: 2019/4/3 且这个模块应该是对于这个用户对是原子的
//            //保存关注双方的用户id对
//            userFollowMapper.insert(userFollow);
//            //被关注者的粉丝数+1
//            userMapper.updateByIdIncrFollowers(userFollow.getFollowederId());
//            //关注者的关注数+1
//            userMapper.updateByIdIncrFollowings(userFollow.getFollowerId());
//            //关注成功
//            return ServerResponse.createBySuccess("follow success");
//        }
//        //否则表明已经关注，进行取关操作
//        else {
//            // TODO: 2019/4/3 这里应该是通过事务管理确保数据完整性
//            // TODO: 2019/4/3 且这个模块应该是对于这个用户对是原子的
//            //删除关注双方的用户id对
//            userFollowMapper.deleteByFollowederIdAndFollowerId(userFollow);
//            //被关注者的粉丝数-1
//            userMapper.updateByIdDecrFollowers(userFollow.getFollowederId());
//            //关注者的关注数-1
//            userMapper.updateByIdDecrFollowings(userFollow.getFollowerId());
//            //取消关注成功
//            return ServerResponse.createBySuccess("unfollow success");
//        }
        return null;
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

    //关注用户
    public ServerResponse follow(User user, Integer followederId){

        //从上面copy的
        //发出此动作的用户一定是关注者
        //所以此用户的id应该等于关注者id
        //否则返回越权操作错误
        if (user.getId() == null || user.getId().equals(followederId)) {
            return ServerResponse.create(
                    ResponseCode.UNAUTHORIZED_OPERATION.getCode(), ResponseCode.UNAUTHORIZED_OPERATION.getDesc());
        }

        //检查被关注者是否存在数据库里
        int rowCount = userMapper.selectCountById(followederId);
        //被关注者不存在
        if(rowCount < 1) {
            return ServerResponse.createByErrorMessage("followeder not exist");
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setFollowederId(followederId);//被关注人的id
        userFollow.setFollowerId(user.getId());//关注人的id

        //查找数据库里是否有关注双方的id对
        int rowCount2 = userFollowMapper.selectCountByFollowederIdAndFollowerId(userFollow);
        //小于1表明未关注，进行关注操作
        // TODO: 2019/4/3 由于对事务不熟悉，这块就先不完善
        if (rowCount2 < 1) {
            // TODO: 2019/4/3 这里应该是通过事务管理确保数据完整性
            // TODO: 2019/4/3 且这个模块应该是对于这个用户对是原子的
            //保存关注双方的用户id对
            userFollowMapper.insert(userFollow);
            //被关注者的粉丝数+1
            userMapper.updateByIdIncrFollowers(userFollow.getFollowederId());
            //关注者的关注数+1
            userMapper.updateByIdIncrFollowings(userFollow.getFollowerId());
            //关注成功
            return ServerResponse.createBySuccess("follow success");
        }
        //否则表明已经关注，进行取关操作
        else {
            // TODO: 2019/4/3 这里应该是通过事务管理确保数据完整性
            // TODO: 2019/4/3 且这个模块应该是对于这个用户对是原子的
            //删除关注双方的用户id对
            userFollowMapper.deleteByFollowederIdAndFollowerId(userFollow);
            //被关注者的粉丝数-1
            userMapper.updateByIdDecrFollowers(userFollow.getFollowederId());
            //关注者的关注数-1
            userMapper.updateByIdDecrFollowings(userFollow.getFollowerId());
            //取消关注成功
            return ServerResponse.createBySuccess("unfollow success");
        }
    }


    /**
     * 获取用户新通知的事件列表
     *
     * @param user 发出请求的用户
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户关注的事件列表
     */
    public ServerResponse NewInform(User user, int pageNum, int pageSize){
        //发出此动作的一定是登陆的用户
        //否则越权操作
        if (user.getId() == null) {
            return ServerResponse.create(
                    ResponseCode.UNAUTHORIZED_OPERATION.getCode(), ResponseCode.UNAUTHORIZED_OPERATION.getDesc());
        }
        PageInfo<UserNoticeVo> Newmasg = new PageInfo<>(
                getUserNewNoticeVoList(user, pageNum, pageSize));
        return ServerResponse.createBySuccess(Newmasg);
    }

    private List<UserNoticeVo> getUserNewNoticeVoList(User user, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<UserNoticeVo> UserNewNoticeVoList = new ArrayList<>();
        UserNotice userNotice = new UserNotice();
        userNotice.setUserId(user.getId());
        userNotice.setView(0);
        if (userNoticeMapper.selectCountByViewAndUserId(userNotice) != null &&userNoticeMapper.selectCountByViewAndUserId(userNotice).size() != 0) {
            List<UserNotice> NewmasgList = userNoticeMapper.selectCountByViewAndUserId(userNotice);
            for (UserNotice i : NewmasgList){
                UserNewNoticeVoList.add(assembleUserNoticeVo(i));
            }
            userNotice.setView(1);
            //用户查看了消息，将View字段更新为1 （用户id，view为0并且信息相同的）
            for (int i = 0; i < NewmasgList.size(); i++) {
                UserNotice massage = NewmasgList.get(i);
                userNotice.setContent(massage.getContent());
                userNoticeMapper.updateByUser_id(userNotice);
            }
            return UserNewNoticeVoList;
        }
        else {
            return null;
        }
    }

        /**
     * 获取用户所有通知的事件列表
     *
     * @param user 发出请求的用户
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户关注的事件列表
     */
    public ServerResponse AllInform(User user, int pageNum, int pageSize) {
        PageInfo<UserNoticeVo> result = new PageInfo<>(getUserNoticeVoList(user, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    private List<UserNoticeVo> getUserNoticeVoList(User user, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<UserNoticeVo> UserNoticeVoList = new ArrayList<>();
        if (userNoticeMapper.selectCountByUserId(user.getId()) != null && userNoticeMapper.selectCountByUserId(user.getId()).size() != 0){
            List<UserNotice> userNoticeList = userNoticeMapper.selectCountByUserId(user.getId());
            for (UserNotice i : userNoticeList){
                UserNoticeVoList.add(assembleUserNoticeVo(i));
            }
            return UserNoticeVoList;
        }
        else
            return null;
    }

    /**
     * 装配UserNoticeVo
     *
     * @param userNotice
     * @return
     */
    private UserNoticeVo assembleUserNoticeVo(UserNotice userNotice){
        UserNoticeVo userNoticeVo = new UserNoticeVo();
        userNoticeVo.setCommenterId(userNotice.getCommenterId());
        userNoticeVo.setContent(userNotice.getContent());
        userNoticeVo.setCreateTime(DateTimeUtil.dateToStr(userNotice.getCreateTime()));
        userNoticeVo.setFollowerId(userNotice.getFollowerId());
        userNoticeVo.setId(userNotice.getId());
        userNoticeVo.setIncidentId(userNotice.getIncidentId());
        userNoticeVo.setIncidentScndCommentId(userNotice.getIncidentScndCommentId());
        userNoticeVo.setNoticeTime(DateTimeUtil.dateToStr(userNotice.getNoticeTime()));
        userNoticeVo.setTitle(userNotice.getTitle());
        userNoticeVo.setType(userNotice.getType());
        userNoticeVo.setUpdateTime(DateTimeUtil.dateToStr(userNotice.getUpdateTime()));
        userNoticeVo.setUserId(userNotice.getUserId());
        userNoticeVo.setView(userNotice.getView());
        return userNoticeVo;
    }
}
//
//public ServerResponse getViewList(Integer userId, int pageNum, int pageSize) {
//    PageInfo<IncidentViewVo> result = new PageInfo<>(getIncidentViewVoList(userId, pageNum, pageSize));
//    return ServerResponse.createBySuccess(result);
//}
//
//private List<IncidentViewVo> getIncidentViewVoList(Integer userId, int pageNum, int pageSize) {
//    PageHelper.startPage(pageNum, pageSize);
//    List<IncidentView> incidentViewList = incidentViewMapper.selectByUserId(userId);
//    if (incidentViewList == null) {
//        return null;
//    }
//    List<IncidentViewVo> incidentViewVoList = new ArrayList<>();
//    for (IncidentView i : incidentViewList) {
//        Incident incident = incidentMapper.selectByPrimaryKey(i.getIncidentId());
//        incidentViewVoList.add(assembleIncidentViewVo(i, incident));
//    }
//    return incidentViewVoList;
//}
//
//    private IncidentVo assembleIncidentVo(Incident incident) {
//        IncidentVo incidentVo = new IncidentVo();
//        incidentVo.setId(incident.getId());
//        incidentVo.setAnon(incident.getAnon());
//        incidentVo.setViews(incident.getViews());
//        incidentVo.setAttentions(incident.getAttentions());
//        incidentVo.setComments(incident.getComments());
//        incidentVo.setTitle(incident.getTitle());
//        incidentVo.setOccurTime(DateTimeUtil.dateToStr(incident.getOccurTime()));
//        incidentVo.setAddress(incident.getAddress());
//        incidentVo.setAddressName(incident.getAddressName());
//        incidentVo.setDescription(incident.getDescription());
//        incidentVo.setMainImageUrl(incident.getMainImageUrl());
//        incidentVo.setMainVideoUrl(incident.getMainVideoUrl());
//        incidentVo.setLatitude(incident.getLatitude());
//        incidentVo.setLongitude(incident.getLongitude());
//        return incidentVo;
//    }




//    //新消息通知
//    public ServerResponse Inform(User user){
//        //发出此动作的一定是登陆的用户
//        //否则越权操作
//        if (user.getId() == null) {
//            return ServerResponse.create(
//                    ResponseCode.UNAUTHORIZED_OPERATION.getCode(), ResponseCode.UNAUTHORIZED_OPERATION.getDesc());
//        }
//        UserNotice userNotice = new UserNotice();
//        userNotice.setUserId(user.getId());
//        userNotice.setView(0);
//        //用user的id去user_notice表中找到对应被通知用户的id，如果有，且消息没看，则返回消息内容
//        if (userNoticeMapper.selectCountByViewAndUserId(userNotice) != null &&userNoticeMapper.selectCountByViewAndUserId(userNotice).size() != 0) {
//                List<String> masg = userNoticeMapper.selectCountByViewAndUserId(userNotice);
//                userNotice.setView(1);
//                //用户查看了消息，将View字段更新为1
//                for (int i = 0; i < masg.size(); i++) {
//                    String massage = masg.get(i);
//                    userNotice.setContent(massage);
//                    userNoticeMapper.updateByUser_id(userNotice);
//                }
//                return ServerResponse.createBySuccess(masg);
//        }
//        else {
//            return ServerResponse.createBySuccess("no message");
//        }
//    }


//    //所有消息通知
//    public ServerResponse AllInform(User user){
//        //发出此动作的一定是登陆的用户
//        //否则越权操作
//        if (user.getId() == null) {
//            return ServerResponse.create(
//                    ResponseCode.UNAUTHORIZED_OPERATION.getCode(), ResponseCode.UNAUTHORIZED_OPERATION.getDesc());
//        }
//        UserNotice userNotice = new UserNotice();
//        userNotice.setUserId(user.getId());
//        //将有关此用户的消息全都显示出来
//        if (userNoticeMapper.selectCountByUserId(userNotice) != null && userNoticeMapper.selectCountByUserId(userNotice).size() != 0){
//            List<String> Allmasg = userNoticeMapper.selectCountByUserId(userNotice);
//            return  ServerResponse.createBySuccess(Allmasg);
//        }
//        else
//            return ServerResponse.createBySuccess("no Message");
//    }

