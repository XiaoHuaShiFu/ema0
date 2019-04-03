package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.User;
import com.ema.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-03-29 19:15
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 登录
     * 如果状态码为0为登录成功
     * 如果状态码为6表明参数code为空
     * 如果状态码为13表明登录失败，需要让用户再次登录
     * 如果状态码为19用户不存在
     *
     * @param code 微信小程序登录时获取的 code
     * @return 如果登录成功返回用户信息
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse login(String code, HttpSession session) {
        ServerResponse serverResponse = iUserService.login(code);
        //如果登录成功，把用户添加进入会话
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.LOGINING_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 用户注册
     *
     * 如果状态码为0表明注册成功
     * 如果状态码为1表明注册失败，因为数据库操作失误
     * 如果状态码为17表明注册失败，因为没有openid
     * 如果状态码为18表明用户已经存在
     *
     * @param code 微信小程序登录时获取的 code
     * @param user 用户注册时附带的信息
     * @return 返回带响应码的提示信息
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public ServerResponse register(String code, User user) {
        return iUserService.register(code, user);
    }

    /**
     * 更新用户个人信息
     * 如果状态码为0表示更新成功
     * 如果状态码为1表示更新失败
     * 如果状态码为10表明用户未登录，需要提示用户进行登录
     *
     * @param session 会话
     * @param user 用户信息列表
     * @return 更新后的用户信息
     */
    @RequestMapping(value = "update_information.do")
    public ServerResponse updateInformation(HttpSession session, User user) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        user.setId(sessionUser.getId()); //传过来的user是没有id的
        ServerResponse response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.LOGINING_USER, response.getData());
        }
        return response;
    }

    /**
     * 获取用户信息，需要用户已经登录
     * 如果状态码为10表明用户未登录，需要提示用户进行登录
     *
     * @param session 会话
     * @return 用户信息
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    public ServerResponse<User> getInformation(HttpSession session) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return ServerResponse.createBySuccess(sessionUser);
    }

    /**
     * 用户登出
     * 如果状态码为20表明退出成功
     *
     * @param session 会话
     * @return 带状态码的消息
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.LOGINING_USER);
        return  ServerResponse.create(
                ResponseCode.LOGOUT_SUCCESS.getCode(), ResponseCode.LOGOUT_SUCCESS.getDesc());
    }

    /**
     * 获得用户当前所处状态
     * 如果状态码为14表明用户未登录
     * 如果状态码为15表明用户已经登录
     *
     * @param session 会话
     * @return 返回带状态码的响应
     */
    @RequestMapping(value = "check_status.do")
    public ServerResponse checkStatus(HttpSession session) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        //用户未登录
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NOT_LOGIN.getCode(), ResponseCode.NOT_LOGIN.getDesc());
        }
        //已经登录
        return ServerResponse.create(ResponseCode.HAVE_LOGGED.getCode(), ResponseCode.HAVE_LOGGED.getDesc());
    }

    /**
     * 上传用户头像
     * 如果状态码为234表明上传文件失败
     * 如果状态码为1表明更新用户信息出错
     * 如果状态码为0表明上传成功
     *
     * @param session 会话
     * @param avatar 用户头像
     * @param request 请求
     * @return 如果成功返回带有用户头像的url
     */
    @RequestMapping(value = "update_avatar.do")
    public ServerResponse updateAvatar(HttpSession session, MultipartFile avatar, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload"); //会创建在webapp文件夹下
        return iUserService.uploadAvatar(path, avatar, sessionUser);
    }

}
