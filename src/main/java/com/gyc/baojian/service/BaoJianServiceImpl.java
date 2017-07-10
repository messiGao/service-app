package com.gyc.baojian.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyc.baojian.mapper.BaoJianMapper;
import com.gyc.common.Constants;
import com.gyc.dto.baojian;
import com.gyc.dto.baojianFile;
import com.gyc.dto.section;
import com.gyc.dto.yanpan;
import com.gyc.dto.yanpanFile;
import com.gyc.yanpan.YanPanController;
import com.gyc.yanpan.service.YanPanService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class BaoJianServiceImpl implements BaoJianService {

	private static final Logger Logger = LoggerFactory.getLogger(BaoJianServiceImpl.class);

	@Autowired
	private BaoJianMapper baoJianMapper;
	
	@Autowired
	private YanPanService yanPanService;

	@Override
	public section submitBaojian(baojian baojian) throws Exception {
		Logger.info("保存保健表数据开始");
		try {
			baojian.setCreated_at(System.currentTimeMillis() / 1000);
			baojian.setUpdated_at(System.currentTimeMillis() / 1000);
			baojian.setStatus(0);
			baoJianMapper.saveBaojianProgram(baojian);
			baojian newbaojian = baoJianMapper.getBaoJianProgram(baojian);

			if (newbaojian == null) {
				Logger.error("保存报建表数据失败，查询报建表数据为空");
				throw new Exception();
			}

			// 根据获得的baojian对象再来处理section对象，默认提交阶段的sectionname为submitsection
			Integer baojian_id = newbaojian.getId();
			section section = new section();
			section.setBaojian_id(baojian_id);
			// 阶段默认未审核中
			section.setStage(0);
			section.setName("submitsection");
			section.setCreated_at(System.currentTimeMillis() / 1000);
			section.setUpdated_at(System.currentTimeMillis() / 1000);

			baoJianMapper.saveSection(section);
			// 返回对应的sectionid
			List<section> sectionlist = baoJianMapper.getSection(section);
			if (sectionlist == null) {
				Logger.error("保存报建表数据失败，查询区域表数据为空");
				throw new Exception();
			}
			section = sectionlist.get(0);

			return section;
		} catch (Exception e) {
			Logger.error("保存保健表数据失败" + e);
			throw e;
		}
	}

	@Override
	public void saveBaoJianFiles(Integer section_id, List<HashMap> pathList) throws Exception {
		Logger.info("保存研判表文件数据开始");
		baojianFile baojianFile = new baojianFile();
		String path = "";
		String prefix = "";
		try {
			for (HashMap pathmap : pathList) {
				baojianFile.setType((String) pathmap.get("type"));
				path = (String) pathmap.get("path");
				prefix = path.substring(path.lastIndexOf(".") + 1);
				baojianFile.setSection_id(section_id);
				baojianFile.setFile_type(prefix);
				baojianFile.setSrc(path);
				baojianFile.setCreated_at(System.currentTimeMillis() / 1000);
				baojianFile.setUpdated_at(System.currentTimeMillis() / 1000);
				baoJianMapper.saveBaojianFile(baojianFile);
			}
		} catch (Exception e) {
			Logger.error("保存研判表文件数据失败" + e);
			throw e;
		}

	}

	@Override
	public Map<String, Object> getBaojianAuditProgram(String id) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// 先获取报建主表信息
			baojian baojian = new baojian();
			baojian.setId(new Integer(id));
			baojian = baoJianMapper.getBaoJianProgram(baojian);
			
			//更新下阅读标记
			yanPanService.updateProgramReadStatus("1",id);

			// 获取section信息
			section section = new section();
			section.setBaojian_id(new Integer(id));
			section.setName("submitsection");
			section.setStage(0);
			List<section> sectionlist = baoJianMapper.getSection(section);
			if (sectionlist == null) {
				tempMap.put("data", data);
				tempMap.put("code", Constants.SUCCESS);
				return tempMap;
			}
			section = sectionlist.get(0);

			// 根据section获取对应的file表信息
			List<baojianFile> baojianFileList = baoJianMapper.getBaojianFileList(section.getId());
			data.put("baojian", baojian);
			data.put("baojianFileList", baojianFileList);
			tempMap.put("data", data);
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询报建宝审核中项目详情失败");
			Logger.error("查询报建宝审核中项目详情" + tempMap + e);
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> getBaojianStage(String id, String stage) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// 先获取报建主表信息
			baojian baojian = new baojian();
			baojian.setId(new Integer(id));
			baojian = baoJianMapper.getBaoJianProgram(baojian);
			
			//更新下阅读标记
			yanPanService.updateProgramReadStatus("1",id);
			
			data.put("baojian", baojian);
			// 获取section信息
			section section = new section();
			section.setBaojian_id(new Integer(id));
			section.setStage(new Integer(stage));
			List<section> tempsectionlist = baoJianMapper.getSection(section);
			if (tempsectionlist == null) {
				Logger.info("查询出来的sectionlist为空，需关注");
				tempMap.put("data", data);
				tempMap.put("code", Constants.SUCCESS);
				return tempMap;
			}

			// 新建一个list，将组装好file信息的section放进来
			List<section> sectionlist = new ArrayList();
			for (section tempsection : tempsectionlist) {
				List<baojianFile> fileList = baoJianMapper.getBaojianFileList(tempsection.getId());
				tempsection.setFileList(fileList);
				sectionlist.add(tempsection);
			}
			data.put("sectionlist", sectionlist);
			tempMap.put("data", data);
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询报建宝项目阶段详情失败");
			Logger.error("查询报建宝项目阶段详情失败" + tempMap + e);
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> saveSection(String baojianId, String stage, List<HashMap> tempSectionList) {
		Map<String, Object> tempMap = new HashMap<String, Object>();

		try {
			// 支持重复提交编辑，每次提交先清空之前数据，再重新插入
			// 根据baojianid和stage进行删除
			section paramSection = new section();
			paramSection.setBaojian_id(new Integer(baojianId));
			paramSection.setStage(new Integer(stage));
			baoJianMapper.deleteSection(paramSection);

			// 重新遍历插入sectionList以及对应的fileList
			for (HashMap<String, Object> tempSectionMap : tempSectionList) {
				// 插入section
				section tempSection = new section();
				tempSection.setBaojian_id(new Integer(baojianId));
				tempSection.setStage(new Integer(stage));
				tempSection.setDisplay_order((String) tempSectionMap.get("display_order"));
				tempSection.setDesc((String) tempSectionMap.get("desc"));
				tempSection.setName((String) tempSectionMap.get("name"));
				baoJianMapper.saveSection(tempSection);

				section newsection = baoJianMapper.getSection(tempSection).get(0);
				// 循环插入fileList
				JSONArray fileListArray = (JSONArray) tempSectionMap.get("fileList");
				for (int i = 0; i < fileListArray.size(); i++) {
					JSONObject jobj = (JSONObject) fileListArray.get(i);
					baojianFile tempfile = new baojianFile();
					String src = (String) jobj.get("src");
					tempfile.setSection_id(newsection.getId());
					tempfile.setSrc(src);
					tempfile.setFile_type(src.substring(src.lastIndexOf(".") + 1));
					tempfile.setType((String) jobj.get("type"));
					tempfile.setCreated_at(System.currentTimeMillis() / 1000);
					tempfile.setUpdated_at(System.currentTimeMillis() / 1000);
					baoJianMapper.saveBaojianFile(tempfile);
				}

			}
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交更新报建宝阶段数据失败" + e);
			Logger.error("提交更新报建宝阶段数据失败,baojianId=" + baojianId + "satge=" + stage + e);
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> getBaojianTips(String section_id) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		// 获取tips信息
		String tip = baoJianMapper.getBaojianTips(section_id);
		data.put("tip", tip);
		tempMap.put("data", data);
		return tempMap;
	}

	@Override
	public Map<String, Object> submitNextStage(baojian baojian) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			Integer status = baojian.getStatus();
			if (status != null)
				status = status + 1;
			baojian.setStatus(status);
			baoJianMapper.updateBaoJianStatus(baojian);
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交报建宝下一阶段失败" + e);
			Logger.error("提交报建宝下一阶段失败" + e);
		}
		return tempMap;
	}

}