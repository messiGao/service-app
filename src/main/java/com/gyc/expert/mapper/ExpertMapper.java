package com.gyc.expert.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gyc.dto.expert;
import com.gyc.dto.question;
import com.gyc.dto.questionDetail;
import com.gyc.dto.yanpan;

@Mapper
public interface ExpertMapper {
	public List<expert> getExpertList(Map<String, Object> expertMap);

	public List<question> getQuestionListNoAnswer(Map<String, Object> expertMap);

	public List<question> getQuestionListWithAnswer(Map<String, Object> expertMap);

	public void saveQuestion(question question);

	public HashMap getQuestionDetail(String id);

	public void saveAnswer(questionDetail questionDetail);

	public void saveExpert(expert expert);

	public List<question> getNeedAnswerListNoAnswer(Map<String, Object> expertMap);

	public List<question> getNeedAnswerListWithAnswer(Map<String, Object> expertMap);

	public void updateExpert(expert expert);

	public List<expert> getExpertByName(String name);

	public void updateQuestion(question question);

	public List<yanpan> getQuestionList(Map<String, Object> paramMap);


}
