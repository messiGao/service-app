package com.gyc.baojian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gyc.baojian.service.BaoJianService;
import com.gyc.common.Constants;
import com.gyc.dto.baojian;
import com.gyc.dto.section;
import com.gyc.dto.user;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class BaoJianController {

	private static final Logger Logger = LoggerFactory.getLogger(BaoJianController.class);

	@Autowired
	BaoJianService baoJianService;

	/* 提交报建宝审批 */
	@RequestMapping(value = "/baojianProgram", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> baojianProgramSubmit(HttpServletRequest req) throws Exception {
		Logger.info("提交报建宝审批数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String user_id = (String) req.getParameter("userId");
			String name = (String) req.getParameter("name");
			String desc = (String) req.getParameter("desc");
			if (desc.length() > 254 || name.length() > 254) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "提交报建宝审批数据失败，名称或描述超过最大长度");
				Logger.error("提交报建宝审批数据失败，名称或描述超过最大长度");
				return tempMap;
			}

			String pictureList = (String) req.getParameter("picturesPathArray");
			Logger.info("pictureList数据" + pictureList);
			// 解决转义字符问题
			pictureList = StringEscapeUtils.unescapeJava(pictureList);
			// 解决转义之后还存在的首尾双引号问题
			if (pictureList.indexOf("\"") == 0)
				pictureList = pictureList.substring(1, pictureList.length());
			if (pictureList.lastIndexOf("\"") == (pictureList.length() - 1))
				pictureList = pictureList.substring(0, pictureList.length() - 1);
			Logger.info("转换后的pictureList数据" + pictureList);

			JSONArray array = JSONArray.fromObject(pictureList);

			Logger.info("转换后的array数据" + array);
			List<HashMap> pathList = new ArrayList();

			for (int i = 0; i < array.size(); i++) {
				HashMap<String, Object> pathMap = new HashMap<String, Object>();
				JSONObject jobj = (JSONObject) array.get(i);
				String path = (String) jobj.get("path");
				pathMap.put("path", path);
				pathMap.put("type", (String) jobj.get("type"));
				pathList.add(pathMap);
			}
			// 先保存报建表主表数据
			baojian baojian = new baojian();
			baojian.setName(name);
			baojian.setDesc(desc);
			baojian.setUser_id(new Integer(user_id));
			section section = baoJianService.submitBaojian(baojian);

			// 再落地文件表数据，文件表都按照section中转落地
			if (section != null) {
				baoJianService.saveBaoJianFiles(section.getId(), pathList);
			}
			tempMap.put("code", Constants.SUCCESS);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交报建宝审批数据");
			Logger.error("提交报建宝审批数据" + tempMap + e);
		}
		return tempMap;
	}

	/* 查询报建宝审核中项目详情 */
	@RequestMapping(value = "/baojianAuditProgram/{id}", method = RequestMethod.GET)
	public Map<String, Object> getBaojianAuditProgram(HttpServletRequest req, @PathVariable String id)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = baoJianService.getBaojianAuditProgram(id);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询报建宝审核中项目详情失败");
			Logger.error("查询报建宝审核中项目详情" + tempMap + e);
		}
		return tempMap;

	}

	/* 查询报建宝各阶段项目详情 */
	@RequestMapping(value = "/baojianStage/{id}/{stage}", method = RequestMethod.GET)
	public Map<String, Object> getBaojianStage(HttpServletRequest req, @PathVariable String id,
			@PathVariable String stage) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = baoJianService.getBaojianStage(id, stage);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询报建宝审核中项目详情失败");
			Logger.error("查询报建宝审核中项目详情" + tempMap + e);
		}
		return tempMap;
	}

	/* 查询报建宝各section提示信息 */
	@RequestMapping(value = "/tips/{section_id}", method = RequestMethod.GET)
	public Map<String, Object> getBaojianTips(HttpServletRequest req, @PathVariable String section_id)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = baoJianService.getBaojianTips(section_id);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "查询报建宝各section提示信息失败");
			Logger.error("查询报建宝各section提示信息失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 提交报建宝各阶段数据 */
	@RequestMapping(value = "/baojianStage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> baojianStageSubmit(HttpServletRequest req) throws Exception {
		Logger.info("提交报建宝各阶段数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();

		try {
			String baojianId = req.getParameter("baojianId");
			String stage = req.getParameter("stage");
			String sectionlist = req.getParameter("sectionArray");
			Logger.info("sectionlist数据" + sectionlist);
			// 解决转义字符问题
			sectionlist = StringEscapeUtils.unescapeJava(sectionlist);
			// 解决转义之后还存在的首尾双引号问题
			if (sectionlist.indexOf("\"") == 0)
				sectionlist = sectionlist.substring(1, sectionlist.length());
			if (sectionlist.lastIndexOf("\"") == (sectionlist.length() - 1))
				sectionlist = sectionlist.substring(0, sectionlist.length() - 1);
			Logger.info("转换后的sectionlist数据" + sectionlist);

			List<HashMap> tempSectionList = new ArrayList();
			JSONArray array = JSONArray.fromObject(sectionlist);
			for (int i = 0; i < array.size(); i++) {
				HashMap<String, Object> tempSectionMap = new HashMap<String, Object>();
				JSONObject jobj = (JSONObject) array.get(i);
				String section_id = (String) jobj.get("section_id");
				String display_order = (String) jobj.get("display_order");
				String name = (String) jobj.get("name");
				String desc = (String) jobj.get("desc");
				if (!org.apache.commons.lang3.StringUtils.isBlank(desc)
						&& !org.apache.commons.lang3.StringUtils.isBlank(name)) {
					if (desc.length() > 254 || name.length() > 254) {
						tempMap.put("code", Constants.REMOTEFAIL);
						tempMap.put("msg", "提交报建宝各阶段数据，名称或描述超过最大长度");
						Logger.error("提交报建宝各阶段数据，名称或描述超过最大长度");
						return tempMap;
					}
				}
				JSONArray fileList = (JSONArray) jobj.get("fileList");
				tempSectionMap.put("section_id", section_id);
				tempSectionMap.put("display_order", display_order);
				tempSectionMap.put("name", name);
				tempSectionMap.put("desc", desc);
				tempSectionMap.put("fileList", fileList);
				tempSectionList.add(tempSectionMap);
			}
			// 插入section数据和对应的file数据
			tempMap = baoJianService.saveSection(baojianId, stage, tempSectionList);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交报建宝各阶段数据失败");
			Logger.error("提交报建宝各阶段数据失败" + tempMap + e);
		}
		return tempMap;
	}

	/* 提交下一阶段 */
	@RequestMapping(value = "/nextStage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> submitNextStage(HttpServletRequest req) throws Exception {
		Logger.info("提交报建宝各阶段数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			baojian baojian = new baojian();
			String baojianId = req.getParameter("baojianId");
			String status = req.getParameter("status");
			if (new Integer(status) > 4) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "提交报建宝提交下一阶段失败,当前阶段超过范围");
				Logger.error("提交报建宝提交下一阶段失败,当前阶段超过范围");
				return tempMap;
			}

			baojian.setId(new Integer(baojianId));
			baojian.setStatus(new Integer(status));
			tempMap = baoJianService.submitNextStage(baojian);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交报建宝提交下一阶段失败");
			Logger.error("提交报建宝提交下一阶段失败" + tempMap + e);
		}
		return tempMap;
	}

}
