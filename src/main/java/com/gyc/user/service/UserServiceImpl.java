package com.gyc.user.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyc.common.Constants;
import com.gyc.common.RedisUtils;
import com.gyc.dto.feedback;
import com.gyc.dto.user;
import com.gyc.dto.verify;
import com.gyc.sms.JavaSmsApi;
import com.gyc.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private static String ENCODING = "UTF-8";

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisUtils redisUtils;

	@Override
	public user getUserInfo(user user) throws Exception {
		return userMapper.getUserInfo(user);
	}

	@Override
	public Map<String, Object> userRegister(user user) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();

		// 根据用户名和对应的密码hashcode去数据库提交并返回对应的值
		user usertemp1 = userMapper.getUserInfo(user);
		if (usertemp1 != null) {
			logger.info("userId already exist,userId:" + usertemp1.getUserId());
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "对不起，你的用户名已存在");
		} else {
			user usertemp = new user();
			long datenow = System.currentTimeMillis();
			usertemp.setCreated_at(datenow / 1000);
			usertemp.setUpdated_at(datenow / 1000);
			usertemp.setPassword_hash(user.getPassword_hash());
			usertemp.setUsername(user.getUsername());
			userMapper.userRegister(usertemp);

			// 注册成功之后调登陆接口
			tempMap = userLogin(user);
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> userLogin(user user) throws Exception {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String username = user.getUsername();
		Integer loginTimes = 0;

		try {
			user = userMapper.getUserInfo(user);

			Object login = redisUtils.get(Constants.LOGIN_TIMES + username);
			if (login != null) {
				loginTimes = new Integer(String.valueOf(login));
				if (loginTimes > 4) {
					tempMap.put("code", Constants.REMOTEFAIL);
					tempMap.put("msg", "密码验证次数已经超过5次");
					data.put("loginExceed", true);
					tempMap.put("data", data);
					return tempMap;
				}

			}

			if (user != null) {
				// 存在用户，登陆成功，需要重新生成一个token码，并存在后台
				String accessToken = userMapper.getUUID();
				user.setAccess_token(accessToken);
				user.setUpdated_at(System.currentTimeMillis() / 1000);
				userMapper.updateUserInfo(user);
				user tempuser = new user();
				tempuser.setUsername(user.getUsername());
				user = userMapper.getUserInfo(tempuser);
				tempMap.put("code", Constants.SUCCESS);
				data.put("accessToken", accessToken);
				data.put("user", user);
				tempMap.put("data", data);
				redisUtils.set(Constants.SESSION_KEY_PREFIX + accessToken, user);
				// 登陆成功之后需要清除掉login次数痕迹
				redisUtils.remove(Constants.LOGIN_TIMES + username);
			} else {
				loginTimes = loginTimes + 1;
				// 用户名和密码输错的要通过redis记录错误次数,第一次需设定失效时间，后面不设定
				if (loginTimes == 1) {
					redisUtils.set(Constants.LOGIN_TIMES + username, String.valueOf(loginTimes), (long) 86400);
				} else
					redisUtils.set(Constants.LOGIN_TIMES + username, String.valueOf(loginTimes));
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "用户名或密码不正确");
			}
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "用户登录失败");
			logger.error("用户登录失败" + username + e);
			return tempMap;
		}
	}

	@Override
	public Map<String, Object> userModify(user user) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		try {
			user.setUpdated_at(System.currentTimeMillis() / 1000);
			userMapper.updateUserInfoByToken(user);
			user = userMapper.getUserInfo(user);
			if (user == null) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", " 用户不存在,用户修改信息失败");
				return tempMap;
			}

			tempMap.put("code", Constants.SUCCESS);
			data.put("user", user);
			tempMap.put("data", data);
		} catch (Exception e) {
			logger.error("修改用户信息失败" + e);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "用户修改信息失败");
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> SendVerifyCode(String phone) throws Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// verify verify = new verify();
			String apikey = "018265a90d7121a0ede31eb26aeb18b5";
			String mobile = phone;
			verify verify = (verify) redisUtils.get(Constants.VERIFY_CODE + phone);
			if (verify != null && verify.getVerifyCode() != null) {
				tempMap.put("msg", "验证码仍在有效期内，不再重复发送");
				tempMap.put("code", Constants.SUCCESS);
				return tempMap;
			} else {
				verify = new verify();
				// 生成动态验证码，并且存到redis，设置有效时间为60s
				String code = getNumber6FromMath();
				verify.setVerifyCode(code);
				redisUtils.set(Constants.VERIFY_CODE + phone, verify, (long) 600);
				long tpl_id = 987325;
				String tpl_value = URLEncoder.encode("#code#", ENCODING) + "=" + URLEncoder.encode(code, ENCODING);
				String smsReturnMessage = JavaSmsApi.tplSendSms(apikey, tpl_id, tpl_value, mobile);
				JSONObject myJsonObject = new JSONObject(smsReturnMessage);
				logger.info("smsReturnMessage:" + myJsonObject.get("msg"));
				tempMap.put("msg", myJsonObject.get("msg"));
				tempMap.put("code", Constants.SUCCESS);
				return tempMap;
			}

		} catch (Exception e) {
			logger.error("发送验证码失败" + e);
			throw e;
		}

	}

	public static String getNumber6FromMath() {
		Long xx = Math.round(Math.random() * 1000000);
		while (xx < 100000) {
			xx = Math.round(Math.random() * 1000000);
		}
		return String.valueOf(xx);
	}

	@Override
	public boolean verifyCode(String username, String verifyCode) {
		verify verify = (verify) redisUtils.get(Constants.VERIFY_CODE + username);
		if (verify != null && verifyCode.equals(verify.getVerifyCode())) {
			return true;
		} else
			return false;
	}

	@Override
	public Map<String, Object> userResetPassword(Map<String, Object> paramMap) throws Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验一下password
			String verifyCode = (String) paramMap.get("verifyCode");
			user user = (user) paramMap.get("user");
			if (verifyCode(user.getUsername(), verifyCode)) {
				// 更新数据库密码
				userMapper.updateUserInfo(user);
				tempMap.put("code", Constants.SUCCESS);
				// 密码reset之后可以清除历史登陆错误记录
				redisUtils.remove(Constants.LOGIN_TIMES + user.getUsername());
			} else {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", " 验证码校验不成功，密码重设失败");
			}
			return tempMap;
		} catch (Exception e) {
			logger.error("重置密码失败" + e);
			throw e;
		}
	}

	@Override
	public Map<String, Object> cleanAllRedisCache() {
		List<String> userList = userMapper.getAllUserInfo();
		for (String access_token : userList) {
			redisUtils.remove(Constants.SESSION_KEY_PREFIX + access_token);
		}
		return new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> submitFeedBack(feedback feedback) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			userMapper.submitFeedBack(feedback);
			tempMap.put("code", Constants.SUCCESS);
			return tempMap;
		} catch (Exception e) {
			logger.error("用户提交反馈失败" + e);
			throw e;
		}
	}

}
