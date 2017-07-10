package com.gyc.yanpan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gyc.dto.baojian;
import com.gyc.dto.user;
import com.gyc.dto.yanpan;

public interface YanPanService {

	public yanpan saveYanpan(yanpan yanpan) throws Exception;

	public void saveYanpanFiles(Integer id, List<HashMap> pathList) throws Exception;

	public Map<String, Object> getProgramList(String user_id, String type,String status, int limit,int offset) throws Exception; 

	public Map<String, Object> isUnRead(String userId, String type) throws Exception;

	public void updateProgramReadStatus(String type, String id) throws Exception;

	public Map<String, Object> getYanPanProgram(String id) throws Exception;


}
