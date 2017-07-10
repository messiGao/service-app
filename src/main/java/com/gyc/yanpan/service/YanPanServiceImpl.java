package com.gyc.yanpan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyc.baojian.mapper.BaoJianMapper;
import com.gyc.common.Constants;
import com.gyc.dto.baojian;
import com.gyc.dto.yanpan;
import com.gyc.dto.yanpanFile;
import com.gyc.user.mapper.UserMapper;
import com.gyc.yanpan.YanPanController;
import com.gyc.yanpan.mapper.YanPanMapper;

@Service
public class YanPanServiceImpl implements YanPanService {

	private static final Logger Logger = LoggerFactory.getLogger(YanPanController.class);

	@Autowired
	private YanPanMapper yanPanMapper;

	@Autowired
	private BaoJianMapper baoJianMapper;

	@Override
	public yanpan saveYanpan(yanpan yanpan) throws Exception {
		Logger.info("保存研判表数据开始");
		try {
			yanpan.setCreated_at(System.currentTimeMillis() / 1000);
			yanpan.setUpdated_at(System.currentTimeMillis() / 1000);
			yanPanMapper.saveYanPanProgram(yanpan);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("created_at", yanpan.getCreated_at());
			paramMap.put("Updated_at", yanpan.getUpdated_at());
			paramMap.put("name", yanpan.getName());
			paramMap.put("limit", 1);
			paramMap.put("offset", 0);
			List<yanpan> yanpanList = yanPanMapper.getYanPanProgramList(paramMap);
			if (yanpanList != null) {
				yanpan = yanpanList.get(0);
			}
			return yanpan;
		} catch (Exception e) {
			Logger.error("保存研判表数据失败" + e);
			throw e;
		}
	}

	@Override
	public void saveYanpanFiles(Integer id, List<HashMap> pathList) throws Exception {
		Logger.info("保存研判表文件数据开始");
		yanpanFile yanpanFile = new yanpanFile();
		String path = "";
		String prefix = "";
		try {
			for (HashMap pathmap : pathList) {
				yanpanFile.setType((String) pathmap.get("type"));
				path = (String) pathmap.get("path");
				prefix = path.substring(path.lastIndexOf(".") + 1);
				yanpanFile.setYanpan_id(id);
				yanpanFile.setFile_type(prefix);
				yanpanFile.setSrc(path);
				yanpanFile.setCreated_at(System.currentTimeMillis() / 1000);
				yanpanFile.setUpdated_at(System.currentTimeMillis() / 1000);
				yanPanMapper.saveYanPanFile(yanpanFile);
			}
		} catch (Exception e) {
			Logger.error("保存研判表文件数据失败" + e);
			throw e;
		}
	}

	@Override
	public Map<String, Object> getProgramList(String user_id, String type, String status, int limit, int offset)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			if ("0".equals(type)) {
				//255默认是全选
				if (!"255".equals(status)) {
					paramMap.put("status", status);
				}
				paramMap.put("user_id", user_id);
				paramMap.put("limit", limit);
				paramMap.put("offset", offset);
				List<yanpan> programList = yanPanMapper.getYanPanProgramList(paramMap);
				data.put("programList", programList);
			} else if ("1".equals(type)) {
				if (!"255".equals(status)) {
					paramMap.put("status", status);
				}
				paramMap.put("user_id", user_id);
				paramMap.put("limit", limit);
				paramMap.put("offset", offset);
				List<yanpan> programList = baoJianMapper.getBaoJianProgramList(paramMap);
				data.put("programList", programList);
			}
			tempMap.put("data", data);
			tempMap.put("code", Constants.SUCCESS);
			return tempMap;
		} catch (Exception e) {
			Logger.error("获取项目列表失败" + e);
			throw e;
		}
	}

	@Override
	public Map<String, Object> isUnRead(String userId, String type) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		boolean unread = false;
		if ("0".equals(type)) {
			paramMap.put("user_id", userId);
			paramMap.put("limit", 1);
			paramMap.put("offset", 0);
			paramMap.put("read_status", "1");
			List<yanpan> programList = yanPanMapper.getYanPanProgramList(paramMap);
			if (programList != null && programList.size()>0) {
				unread = true;
			}
			data.put("unread", unread);
		} else if ("1".equals(type)) {
			paramMap.put("user_id", userId);
			paramMap.put("limit", 1);
			paramMap.put("offset", 0);
			paramMap.put("read_status", "1");
			List<yanpan> programList = baoJianMapper.getBaoJianProgramList(paramMap);
			if (programList != null && programList.size()>0) {
				unread = true;
			}
			data.put("unread", unread);
		}
		tempMap.put("data", data);
		tempMap.put("code", Constants.SUCCESS);
		return tempMap;
	}

	@Override
	public void updateProgramReadStatus(String type, String id) throws Exception {
		try {
			if ("0".equals(type)) {
				yanpan yanpan = new yanpan();
				yanpan.setId(new Integer(id));
				yanpan.setRead_status("0");
				yanPanMapper.updateYanPan(yanpan);
			} else if ("1".equals(type)) {
				baojian baojian = new baojian();
				baojian.setId(new Integer(id));
				baojian.setRead_status("0");
				baoJianMapper.updateBaoJian(baojian);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Map<String, Object> getYanPanProgram(String id) throws Exception {
		// 先获取研判主表数据
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			paramMap.put("id", new Integer(id));
			paramMap.put("limit", 1);
			paramMap.put("offset", 0);
			List<yanpan> programList = yanPanMapper.getYanPanProgramList(paramMap);
			if (programList == null) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "没找到该ID对应的项目详情");
				return tempMap;
			}
			
			//更新下阅读标记
			updateProgramReadStatus("0",id);
			
			yanpan yanpan = programList.get(0);

			Integer yanpan_id = yanpan.getId();
			// 再根据研判表主键获取对应的file列表信息
			List<yanpanFile> yanpanFileList = yanPanMapper.getYanPanFileList(yanpan_id);

			data.put("yanpan", yanpan);
			data.put("yanpanFileList", yanpanFileList);
			tempMap.put("data", data);
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			throw e;
		}

		return tempMap;
	}

}
