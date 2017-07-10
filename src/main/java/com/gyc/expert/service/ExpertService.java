package com.gyc.expert.service;

import java.util.Map;

import com.gyc.dto.expert;
import com.gyc.dto.questionDetail;

public interface ExpertService {

	Map<String, Object> getExpertList(int limit, int offset) throws Exception;

	Map<String, Object> getQuestions(String userId, String type,int limit,int offset)  throws Exception;

	Map<String, Object> submitQuestion(String userId, String expertId, String name) throws Exception;

	Map<String, Object> getQuestionDetail(String id) throws Exception;

	Map<String, Object> submitAnswer(questionDetail questionDetail) throws Exception;

	Map<String, Object> newExpert(expert expert) throws Exception;

	Map<String, Object> getNeedAnswerList(String expertId, String type, int limit, int offset) throws Exception;

	Map<String, Object> updateExpert(expert expert) throws Exception;

	Map<String, Object> getExpert(String name);

	Map<String, Object> isQuestionUnRead(String userId) throws Exception;


}
