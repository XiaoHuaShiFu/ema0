package com.ema.common;

/**
 * Created by lenovo on 2019/1/31.
 */
public final class Const {

    private Const() {}

    public static final String LOGINING_USER = "LOGINING_USER";
    public static final String QRCODE_LOGINING = "QRCODE_LOGINING";


    public final class QrCode {
        public static final int EXPIRY_TIME = 10000; //二维码失效时间
        public static final int CLEAN_INTERVAL = 1000; //每次清理间隔
    }

    public final class Cookie {
        public static final int EXPIRY_TIME = 120000; //cookie失效时间
        public static final int CLEAN_INTERVAL = 1000; //每次清理间隔
    }


    public final class Duty {
        public static final int SIGN_IN_INTERVAL = 1000; //两次签到的间隔 518400000 -> 6天
    }



}
