package com.ema.common;

/**
 * Created by lenovo on 2019/1/31.
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

    AUTH_CODE_NOT_EXIST(130, "AUTH_CODE_NOT_EXIST"), //认证码不存在
    AUTH_CODE_HAS_BEEN_USED(131, "AUTH_CODE_HAS_BEEN_USED"), //认证码已经被使用
    AUTH_CODE_FORMAT_ERROR(132, "AUTH_CODE_FORMAT_ERROR"), //认证码格式错误

    NOT_AUTH_BY_THE_DEPARTMENT(140, "NOT_AUTH_BY_THE_DEPARTMENT"), //未通过部门认证
    FULLNAME_CAN_NOT_BE_NULL(141, "FULLNAME_CAN_NOT_BE_NULL"), //fullname不能未空
    QRCODE_INVALID(142, "QRCODE_INVALID"), //二维码失效
    TOKEN_INVALID(143, "TOKEN_INVALID"), //token无效
    TOO_SHORT_TIME_INTERVAL(144, "TOO_SHORT_TIME_INTERVAL"), //时间间隔过短
    QRCODE_CAN_NOT_BE_NULL(145, "QRCODE_CAN_NOT_BE_NULL"),// 二维码不能为空

    START_TIME_GREATER_THEN_END_TIME(160, "START_TIME_GREATER_THEN_END_TIME"), //开始时间大于结束时间
    LENDING_TOO_LONG(161, "LENDING_TOO_LONG"), //借用时间过长
    THIS_PERIOD_HAS_BEEN_BORROWED(162, "THIS_PERIOD_HAS_BEEN_BORROWED"), //该时间段已经被借用

    COOKIE_NOT_EXIST(180, "COOKIE_NOT_EXIST"), //cookie不存在


    UPLOAD_FILE_SUCCESS(233, "UPLOAD_FILE_SUCCESS"), //上传文件成功
    UPLOAD_FILE_FAIL(234, "UPLOAD_FILE_FAIL"); //上传文件成功

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
