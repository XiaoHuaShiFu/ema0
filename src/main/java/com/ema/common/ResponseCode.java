package com.ema.common;

/**
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-02-27 20:30
 */
public enum ResponseCode {

    SUCCESS(0, "SUCCESS"), //默认成功
    ERROR(1, "ERROR"), //默认错误
    UNAUTHORIZED_OPERATION(2, "UNAUTHORIZED_OPERATION"), //越权操作

    ILLEGAL_ARGUMENT(3, "ILLEGAL_ARGUMENT"), //非法参数
    ARGUMENT_CAN_NOT_BE_NULL(4, "ARGUMENT_CAN_NOT_BE_NULL"), //参数不能为null
    ARGUMENT_RANGE_ERROR(5, "ARGUMENT_RANGE_ERROR"), //参数范围错误
    ARGUMENT_CAN_NOT_BE_BLANK(6, "ARGUMENT_CAN_NOT_BE_BLANK"), //参数不能为空

    NEED_LOGIN(10, "NEED_LOGIN"), //需要登录
    FIRST_LOGIN(11, "FIRST_LOGIN"), //首次登录
    LOGIN_SUCCESS(12, "LOGIN_SUCCESS"), //登录成功
    LOGIN_FALSE(13, "LOGIN_FALSE"), //登录失败
    NOT_LOGIN(14, "NOT_LOGIN"), //未登录状态
    HAVE_LOGGED(15, "HAVE LOGGED"), //已登录状态
    REGISTER_SUCCESS(16, "REGISTER_SUCCESS"), //注册成功
    REGISTER_FALSE(17, "REGISTER_FALSE"),//注册失败
    USER_EXIST(18, "USER_EXIST"), //用户已经存在
    USER_NOT_EXIST(19, "USER_NOT_EXIST"), //用户不存在
    LOGOUT_SUCCESS(20, "LOGOUT_SUCCESS"), //退出登录成功

    COOKIE_NOT_EXIST(180, "COOKIE_NOT_EXIST"), //cookie不存在

    UPLOAD_FILE_SUCCESS(233, "UPLOAD_FILE_SUCCESS"), //上传文件成功
    UPLOAD_FILE_FAIL(234, "UPLOAD_FILE_FAIL"), //上传文件成功

    THUMB_UP_SUCCESS(280, "THUMB_UP_SUCCESS"), //点赞成功
    CANCEL_THUMB_UP_SUCCESS(281, "CANCEL_THUMB_UP_SUCCESS"), //取消点赞成功
    ATTENTION_SUCCESS(282, "ATTENTION_SUCCESS"), //关注成功
    CANCEL_ATTENTION_SUCCESS(283, "CANCEL_ATTENTION_SUCCESS"), //取消关注成功
    COLLECTION_SUCCESS(284, "COLLECTION_SUCCESS"), //收藏成功
    CANCEL_COLLECTION_SUCCESS(285, "CANCEL_COLLECTION_SUCCESS"); //取消收藏成功

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

}
