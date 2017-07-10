package com.gyc.expert.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyc.common.Constants;
import com.gyc.dto.expert;
import com.gyc.dto.question;
import com.gyc.dto.questionDetail;
import com.gyc.dto.yanpan;
import com.gyc.expert.mapper.ExpertMapper;

@Service
public class ExpertServiceImpl implements ExpertService {

	private static final Logger Logger = LoggerFactory.getLogger(ExpertServiceImpl.class);

	@Autowired
	ExpertMapper expertMapper;

	@Override
	public Map<String, Object> getExpertList(int limit, int offset) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> expertMap = new HashMap<String, Object>();
		expertMap.put("offset", offset);
		expertMap.put("limit", limit);
		List<expert> expertlist = expertMapper.getExpertList(expertMap);
		data.put("expertlist", expertlist);
		tempMap.put("code", Constants.SUCCESS);
		tempMap.put("data", data);
		return tempMap;
	}

	@Override
	public Map<String, Object> getQuestions(String userId, String type,int limit,int offset) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<question> questionlist = new ArrayList();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> expertMap = new HashMap<String, Object>();
		expertMap.put("offset", offset);
		expertMap.put("userId", userId);
		expertMap.put("limit", limit);
		if ("0".equals(type)) {
			questionlist = expertMapper.getQuestionListNoAnswer(expertMap);
		} else if ("1".equals(type)) {
			questionlist = expertMapper.getQuestionListWithAnswer(expertMap);
		}
		data.put("questionlist", questionlist);
		tempMap.put("data", data);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> submitQuestion(String userId, String expertId, String name) throws Exception{
		Map<String, Object> tempMap = new HashMap<String, Object>();
		question question = new question();
		question.setCreated_at(System.currentTimeMillis() / 1000);
		question.setUpdated_at(System.currentTimeMillis() / 1000);
		question.setName(name);
		question.setExpert_id(new Integer(expertId));
		question.setUser_id(new Integer(userId));
		question.setRead_status("0");
		expertMapper.saveQuestion(question);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> getQuestionDetail(String id) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		HashMap question = new HashMap();
		question = expertMapper.getQuestionDetail(id);
		//查看之后更新下已读状态
		if(question!=null){
			question tempquestion = new question();
			tempquestion.setRead_status("0");
			tempquestion.setId((Integer)question.get("id"));
			expertMapper.updateQuestion(tempquestion);
		}
		//expertMapper.updateQuestion
		data.put("question", question);
		tempMap.put("data", data);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> submitAnswer(questionDetail questionDetail) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		expertMapper.saveAnswer(questionDetail);
		//回答之后自动更新为已读
		question question =new question();
		question.setRead_status("1");
		question.setId(questionDetail.getQuestion_id());
		question.setUpdated_at(System.currentTimeMillis() / 1000);
		expertMapper.updateQuestion(question);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> newExpert(expert expert) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		expertMapper.saveExpert(expert);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> getNeedAnswerList(String expertId, String type, int limit, int offset) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> expertMap = new HashMap<String, Object>();
		List<question> answerlist = new ArrayList();
		Map<String, Object> data = new HashMap<String, Object>();
		expertMap.put("limit", limit);
		expertMap.put("offset", offset);
		expertMap.put("expertId", expertId);
		if ("0".equals(type)) {
			answerlist = expertMapper.getNeedAnswerListNoAnswer(expertMap);
		} else if ("1".equals(type)) {
			answerlist = expertMapper.getNeedAnswerListWithAnswer(expertMap);
		}
		data.put("answerlist", answerlist);
		tempMap.put("data", data);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> updateExpert(expert expert) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		expertMapper.updateExpert(expert);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public Map<String, Object> getExpert(String name) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<expert> expertlist = expertMapper.getExpertByName(name);
		data.put("expertlist", expertlist);
		tempMap.put("code", Constants.SUCCESS);
		tempMap.put("data", data);
		return tempMap;
	}

	@Override
	public Map<String, Object> isQuestionUnRead(String userId) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		boolean unread = false;
        paramMap.put("user_id", userId);
	    paramMap.put("limit", 1);
	    paramMap.put("offset", 0);
	    paramMap.put("read_status", "1");
		List<yanpan> questionList = expertMapper.getQuestionList(paramMap);
		if (questionList != null && questionList.size()>0) {
				unread = true;
		}
		data.put("unread", unread);
		tempMap.put("data", data);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}
}