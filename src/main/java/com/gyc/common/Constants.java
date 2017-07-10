package com.gyc.common;

/** 
 * Created by messigao on 2016/12/30. 
 */  
public interface Constants {  
    int MAX_FILE_UPLOAD_SIZE = 5242880;  
    public static final String MOBILE_NUMBER_SESSION_KEY = "phone";  
    public static final String USER_ID_SESSION_KEY = "userId";  
    //httpbasic固定key
    public static final String ACCESS_TOKEN = "Authorization"; 
    //成功返回编码
    public static final String SUCCESS = "0";
    //错误返回编码
    public static final String REMOTEFAIL="50";
    //错误信息
    public static final String ERRORMSG ="Remote service error";
    //session前置标志位
    public final static String SESSION_KEY_PREFIX = "session:";
    //定义验证码对应redis key
    public static final String VERIFY_CODE = "verifyCode:";
    public static final String VERIFY_TIMES = "verifyTimes:";
    public static final String LOGIN_TIMES = "loginTimes:";
}  