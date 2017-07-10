package com.gyc.user.service;

import java.util.Map;

import com.gyc.dto.feedback;
import com.gyc.dto.user;

public interface UserService {
	
	public user getUserInfo(user user) throws Exception;
	
	public Map<String,Object> userRegister(user user) throws Exception;

	public Map<String,Object> userLogin(user user) throws Exception;

	public Map<String, Object> userModify(user user) throws Exception;
	
	public Map<String, Object> SendVerifyCode(String phone) throws Exception;

	public boolean verifyCode(String username,String verifyCode);

	public Map<String, Object> userResetPassword(Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> cleanAllRedisCache();

	public Map<String, Object> submitFeedBack(feedback feedback) throws Exception;

}
