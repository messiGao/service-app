package com.gyc.user;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gyc.common.Constants;
import com.gyc.common.RedisUtils;
import com.gyc.dto.feedback;
import com.gyc.dto.user;
import com.gyc.interceptor.Auth;
import com.gyc.user.service.UserService;
import com.qcloud.cos.sign.Credentials;
import com.qcloud.cos.sign.Sign;

@RestController

public class UserController {
	private static final Logger Logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@Autowired
	private RedisUtils redisUtils;

	@RequestMapping("/verify-code/{phone}")
	@ResponseBody
	/* 发送验证码接口 */
	public Map<String, Object> sendVerifyCode(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String phone) throws Exception {
		Logger.info("发送验证码开始");

		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = userService.SendVerifyCode(phone);
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "验证码发送失败");
			Logger.error("验证码发送失败，手机号：" + phone + e);
			return tempMap;
		}
	}

	@RequestMapping("/phone/{phone}")
	@ResponseBody
	/* 判断该手机号是否已经注册 */
	public Map<String, Object> getPhoneRegister(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String phone) throws Exception {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			user user = new user();
			user.setUsername(phone);
			user = userService.getUserInfo(user);
			if (user != null) {
				data.put("register", true);
				tempMap.put("code", Constants.SUCCESS);
				tempMap.put("data", data);
			} else {
				data.put("register", false);
				tempMap.put("code", Constants.SUCCESS);
				tempMap.put("data", data);
			}
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "判断手机号是否已注册失败");
			Logger.error("判断手机号是否已注册失败，手机号：" + phone + e);
			return tempMap;
		}
	}

	/* 用户注册接口 */
	@RequestMapping(value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> userRegister(HttpServletRequest req) throws Exception {
		Logger.info("用户注册开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();

		try {
			String password = (String) req.getParameter("password");
			String verifyCode = (String) req.getParameter("verifyCode");
			String username = (String) req.getParameter("username");
			if (password == null || verifyCode == null || username == null) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "入参不完整");
				return tempMap;
			}
			// 验证验证码是否有效
			if (userService.verifyCode(username, verifyCode)) {
				user user = new user();
				user.setUsername((String) req.getParameter("username"));
				user.setPassword_hash(String.valueOf(password.hashCode()));
				tempMap = userService.userRegister(user);
			} else {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "验证码校验失败");
				return tempMap;
			}
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "注册失败");
			Logger.error("用户注册失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 用户登录接口 */
	@RequestMapping(value = "/auth", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> userLogin(HttpServletRequest req) throws Exception {
		Logger.info("用户登录开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			user user = new user();
			String password = (String) req.getParameter("password");
			user.setUsername((String) req.getParameter("username"));
			user.setPassword_hash(String.valueOf(password.hashCode()));
			tempMap = userService.userLogin(user);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "登陆失败");
			Logger.error("用户登录失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 获取用户信息 */
	@Auth
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public Map<String, Object> getUserInfo(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String userId) throws Exception {
		Logger.info("获取用户信息开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			user user = new user();
			user.setUserId(new Integer(userId));
			user = userService.getUserInfo(user);
			data.put("user", user);
			tempMap.put("code", Constants.SUCCESS);
			tempMap.put("data", data);
			return tempMap;
		} catch (Exception e) {
			Logger.error("获取用户信息失败，userId：" + userId + e);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取用户信息失败");
			return tempMap;
		}
	}

	@RequestMapping(value = "/auth", method = RequestMethod.DELETE)
	/* 用户退出登录 */
	public Map<String, Object> loginOut(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Logger.info("用户退出登录开始");
		String accessToken = req.getHeader(Constants.ACCESS_TOKEN);
		Map<String, Object> tempMap = new HashMap<String, Object>();

		try {
			user model = (user) redisUtils.get(Constants.SESSION_KEY_PREFIX + accessToken);
			redisUtils.remove(Constants.SESSION_KEY_PREFIX + accessToken);
			user user = new user();
			user.setUserId(model.getUserId());
			user = userService.getUserInfo(user);
			if (user != null) {
				user.setAccess_token(null);
				userService.userModify(user);
			}
			tempMap.put("code", Constants.SUCCESS);
			return tempMap;
		} catch (Exception e) {
			Logger.error("get userinfo error，token：" + accessToken);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "退出登录失败");
			return tempMap;
		}
	}

	
	/* 修改用户资料 */
	@Auth
	@RequestMapping(value = "/user", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> userModify(HttpServletRequest req) throws Exception {
		Logger.info("修改用户资料");
		String accessToken = req.getHeader(Constants.ACCESS_TOKEN);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			user user = new user();
			String password = (String) req.getParameter("password");
			user.setUsername((String) req.getParameter("username"));
			user.setPosition((String) req.getParameter("position"));
			user.setCompany((String) req.getParameter("company"));
			if (org.apache.commons.lang3.StringUtils.isNotBlank(password)) {
				user.setPassword_hash(String.valueOf(password.hashCode()));
			}
			user.setAvatar((String) req.getParameter("avatar"));
			user.setNickname((String) req.getParameter("nickname"));
			user.setAccess_token(accessToken);
			tempMap = userService.userModify(user);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "用户资料修改失败");
			Logger.error("用户资料修改失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 重设密码 */
	@RequestMapping(value = "/password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> userResetPassword(HttpServletRequest req) throws Exception {
		Logger.info("重设密码开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			user user = new user();
			String password = (String) req.getParameter("password");
			user.setUsername((String) req.getParameter("username"));
			user.setPassword_hash(String.valueOf(password.hashCode()));
			String verifyCode = (String) req.getParameter("verifyCode");
			if (org.apache.commons.lang3.StringUtils.isBlank(password)
					|| org.apache.commons.lang3.StringUtils.isBlank((String) req.getParameter("username"))
					|| org.apache.commons.lang3.StringUtils.isBlank(verifyCode)) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "用户名||密码||验证码不可为空");
				return tempMap;
			}

			paramMap.put("user", user);
			paramMap.put("verifyCode", verifyCode);
			tempMap = userService.userResetPassword(paramMap);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "密码重设失败");
			Logger.error("密码重设失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 获取签名信息 */
	@RequestMapping(value = "/sign/{cosPath}", method = RequestMethod.GET)
	public Map<String, Object> getSign(HttpServletRequest req) throws Exception {
		Logger.info("获取腾讯云签名开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			long appId = 1251019826;
			String secretId = "AKIDPhngMqdongOuEMG0SjneNuGSSFIAtKdk";
			String secretKey = "2VovpW1JPIzVouoW1JPJDylSxKJCuPhm";
			String cosPath = req.getParameter("cosPath");
			if (cosPath == null)
				cosPath = "";
			// 初始化秘钥信息
			Credentials cred = new Credentials(appId, secretId, secretKey);
			String bucketName = "xmtimaged01";
			long expired = System.currentTimeMillis() / 1000 + 600;
			String sign = Sign.getPeriodEffectiveSign(bucketName, cosPath, cred, expired);
			data.put("sign", sign);
			tempMap.put("code", Constants.SUCCESS);
			tempMap.put("data", data);
			return tempMap;
		} catch (Exception e) {
			Logger.error("获取腾讯云签名开始，sign：" + e);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取腾讯云签名开始失败");
			return tempMap;
		}
	}

	/* 获取签名信息 */
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public Map<String, Object> getSignNoParam(HttpServletRequest req) throws Exception {
		Logger.info("获取腾讯云签名开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			long appId = 1251019826;
			String secretId = "AKIDPhngMqdongOuEMG0SjneNuGSSFIAtKdk";
			String secretKey = "2VovpW1JPIzVouoW1JPJDylSxKJCuPhm";
			String cosPath = "";
			// 初始化秘钥信息
			Credentials cred = new Credentials(appId, secretId, secretKey);
			String bucketName = "xmtimaged01";
			long expired = System.currentTimeMillis() / 1000 + 600;
			String sign = Sign.getPeriodEffectiveSign(bucketName, cosPath, cred, expired);
			data.put("sign", sign);
			tempMap.put("code", Constants.SUCCESS);
			tempMap.put("data", data);
			return tempMap;
		} catch (Exception e) {
			Logger.error("获取腾讯云签名开始，sign：" + e);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取腾讯云签名开始失败");
			return tempMap;
		}
	}

	/* 清理cache用户信息 */
	@RequestMapping(value = "/cleanAllRedisCache", method = RequestMethod.GET)
	public Map<String, Object> cleanAllRedisCache(HttpServletRequest req) throws Exception {
		Logger.info("清理用户cache开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			tempMap = userService.cleanAllRedisCache();
			tempMap.put("code", Constants.SUCCESS);
			return tempMap;
		} catch (Exception e) {
			Logger.error("清理cache用户信息：" + e);
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "清理用户cache失败");
			return tempMap;
		}
	}
	
	/* 用户反馈信息 */
	@RequestMapping(value = "/feedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> feedBack(HttpServletRequest req) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			feedback feedback = new feedback();
			String user_id = req.getParameter("user_id");
			String content = req.getParameter("content");
			if (content.length() > 3999 || content.length() > 3999) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "提交用户反馈失败，内容超过最大长度");
				Logger.error("提交用户反馈失败，内容超过最大长度");
				return tempMap;
			}
			feedback.setUser_id(new Integer(user_id));
			feedback.setContent(content);
			feedback.setCreated_at(System.currentTimeMillis()/1000);
			feedback.setUpdated_at(System.currentTimeMillis()/1000);
			tempMap = userService.submitFeedBack(feedback);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "用户反馈信息失败");
			Logger.error("用户反馈信息失败" + tempMap + e);
		}
		return tempMap;
	}
	
}
