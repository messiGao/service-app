package com.gyc.expert;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gyc.baojian.BaoJianController;
import com.gyc.baojian.service.BaoJianService;
import com.gyc.common.Constants;
import com.gyc.dto.expert;
import com.gyc.dto.questionDetail;
import com.gyc.dto.user;
import com.gyc.expert.service.ExpertService;
import com.gyc.interceptor.Auth;

@RestController
public class ExpertController {
	
	private static final Logger Logger = LoggerFactory.getLogger(ExpertController.class);
	
	@Autowired
	ExpertService expertService;
	
	/* 新增专家 */
	@RequestMapping(value = "/expert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> newExpert(HttpServletRequest req) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String name = (String) req.getParameter("name");
		    String description = (String) req.getParameter("description");
		    String phone = (String) req.getParameter("phone");
		    String work_time = (String) req.getParameter("work_time");
		    expert expert = new expert();
		    expert.setDescription(description);
		    expert.setName(name);
		    expert.setPhone(phone);
		    expert.setWork_time(work_time);
		    expert.setCreated_at(System.currentTimeMillis() / 1000);
		    expert.setUpdated_at(System.currentTimeMillis() / 1000);
			tempMap = expertService.newExpert(expert);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "新增专家失败");
			Logger.error("新增专家失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 修改专家 */
	@RequestMapping(value = "/expert", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> updateExpert(HttpServletRequest req) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String name = (String) req.getParameter("name");
		    String description = (String) req.getParameter("description");
		    String phone = (String) req.getParameter("phone");
		    String work_time = (String) req.getParameter("work_time");
		    expert expert = new expert();
		    expert.setDescription(description);
		    expert.setName(name);
		    expert.setPhone(phone);
		    expert.setWork_time(work_time);
		    expert.setUpdated_at(System.currentTimeMillis() / 1000);
			tempMap = expertService.updateExpert(expert);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "新增专家失败");
			Logger.error("新增专家失败" + tempMap + e);
		}
		return tempMap;
	}
	/* 获取提问问题列表,type 0 未回答 1 已回答 */
	@RequestMapping(value = "/needAnswer/{expertId}/{type}/{limit}/{offset}", method = RequestMethod.GET)
	public Map<String, Object> getNeedAnswerList(HttpServletRequest req,@PathVariable String expertId,
			@PathVariable String type,@PathVariable int limit, @PathVariable int offset)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.getNeedAnswerList(expertId,type,limit,offset);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取提问问题列表失败");
			Logger.error("获取提问问题列表失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 查询具体专家，名字可能重复，所以返回列表 */
	@RequestMapping(value = "/expert/{name}", method = RequestMethod.GET)
	public Map<String, Object> getExpert(HttpServletRequest req,@PathVariable String name)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.getExpert(name);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询专家列表失败");
			Logger.error("查询专家列表失败" + tempMap + e);
		}
		return tempMap;
	}
	
	
	/* 查询专家列表 */
	@RequestMapping(value = "/experts/{limit}/{offset}", method = RequestMethod.GET)
	public Map<String, Object> getExpertList(HttpServletRequest req,@PathVariable int limit, @PathVariable int offset)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.getExpertList(limit,offset);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询专家列表失败");
			Logger.error("查询专家列表失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 查询我的问题列表,type 0 未回答 1 已回答 */
	@RequestMapping(value = "/questions/{userId}/{type}/{limit}/{offset}", method = RequestMethod.GET)
	public Map<String, Object> getQuestionList(HttpServletRequest req,@PathVariable String userId,
			@PathVariable String type,@PathVariable int limit, @PathVariable int offset)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.getQuestions(userId,type,limit,offset);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询我的问题列表失败");
			Logger.error("查询我的问题列表失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 提交问题 */
	@RequestMapping(value = "/question", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> submitQuestion(HttpServletRequest req) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String userId = (String) req.getParameter("userId");
		    String expertId = (String) req.getParameter("expertId");
		    String name = (String) req.getParameter("questionName");
			tempMap = expertService.submitQuestion(userId,expertId,name);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交问题失败");
			Logger.error("提交问题失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 回答问题 */
	@RequestMapping(value = "/answer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> submitAnswer(HttpServletRequest req) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String question_id = (String) req.getParameter("question_id");
			String userId = (String) req.getParameter("userId");
			String description = (String) req.getParameter("description");
			if (description.length() > 1999 || description.length() > 1999) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "回答问题失败，答复最大长度");
				Logger.error("回答问题失败，答复最大长度");
				return tempMap;
			}
			
			questionDetail questionDetail = new questionDetail();
			questionDetail.setQuestion_id(new Integer(question_id));
			questionDetail.setAnswer_id(new Integer(userId));
			questionDetail.setDescription(description);
			questionDetail.setCreated_at(System.currentTimeMillis() / 1000);
			questionDetail.setUpdated_at(System.currentTimeMillis() / 1000);
			questionDetail.setType(new Integer("1"));
			tempMap = expertService.submitAnswer(questionDetail);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "回答问题失败");
			Logger.error("回答问题失败" + tempMap + e);
		}
		return tempMap;
	}
	
	/* 获取用户是否存在未读接口 */
	@RequestMapping(value = "/questionunread/{userId}", method = RequestMethod.GET)
	public Map<String, Object> isQuestionUnRead(HttpServletRequest req,
			@PathVariable String userId
			) throws Exception {
		Logger.info("获取用户未读项目接口开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.isQuestionUnRead(userId);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取用户未读项目接口失败");
			Logger.error("获取用户未读项目接口失败" + tempMap);
		}
		return tempMap;
	}
	
	/*  获取问题详情*/
	@RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
	public Map<String, Object> getQuestionDetail(HttpServletRequest req,@PathVariable String id
			) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = expertService.getQuestionDetail(id);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取问题详情失败");
			Logger.error("获取问题详情失败" + tempMap + e);
		}
		return tempMap;
	}
	
}
