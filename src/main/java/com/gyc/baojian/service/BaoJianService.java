package com.gyc.baojian.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gyc.dto.baojian;
import com.gyc.dto.section;

public interface BaoJianService {

	section submitBaojian(baojian baojian) throws Exception;

	void saveBaoJianFiles(Integer section_id, List<HashMap> pathList) throws Exception;

	Map<String, Object> getBaojianAuditProgram(String id) throws Exception;

	Map<String, Object> getBaojianStage(String id, String stage);

	Map<String, Object> saveSection(String baojianId, String stage, List<HashMap> tempSectionList);

	Map<String, Object> getBaojianTips(String section_id);

	Map<String, Object> submitNextStage(baojian baojian) throws Exception;

}
