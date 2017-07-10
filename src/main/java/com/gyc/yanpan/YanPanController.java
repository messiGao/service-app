package com.gyc.yanpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gyc.common.Constants;
import com.gyc.dto.yanpan;
import com.gyc.yanpan.service.YanPanService;

@RestController
public class YanPanController {

	private static final Logger Logger = LoggerFactory.getLogger(YanPanController.class);

	@Autowired
	YanPanService yanPanService;

	/* 提交研判宝数据 */
	@RequestMapping(value = "/yanpanProgram", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> yanpanProgramSubmit(HttpServletRequest req) throws Exception {
		Logger.info("提交研判宝数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			String user_id = (String) req.getParameter("userId");
			String name = (String) req.getParameter("name");
			String desc = (String) req.getParameter("desc");
			String pictureList = (String) req.getParameter("picturesPathArray");
			String area_id = (String) req.getParameter("area_id");
			if(desc.length()>254||name.length()>254){
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "提交研判宝数据失败，名称或描述超过最大长度");
				Logger.error("提交研判宝数据失败，名称或描述超过最大长度");
				return tempMap;
			}
			Logger.info("pictureList数据"+pictureList);
			if(pictureList==null){
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "提交研判宝数据失败，pictureList为空");
				Logger.error("提交研判宝数据失败，pictureList为空");
				return tempMap;
			}
		    pictureList = StringEscapeUtils.unescapeJava(pictureList);
		    if(pictureList.indexOf("\"")==0) pictureList = pictureList.substring(1,pictureList.length());
		    if(pictureList.lastIndexOf("\"")==(pictureList.length()-1)) pictureList = pictureList.substring(0,pictureList.length()-1);
		    Logger.info("转换后的pictureList数据"+pictureList);
			JSONArray array = JSONArray.fromObject(pictureList);
			
			Logger.info("转换后的array数据"+array);
			List<HashMap> pathList = new ArrayList();
			
			for (int i = 0; i < array.size(); i++) {
				HashMap<String, Object> pathMap = new HashMap<String, Object>();
				JSONObject jobj = (JSONObject) array.get(i);
				String path = (String) jobj.get("path");
				pathMap.put("path",path);
				pathMap.put("type", (String) jobj.get("type"));
				pathList.add(pathMap);
			}
			// 先保存研判表主表数据
			yanpan yanpan = new yanpan();
			yanpan.setName(name);
			yanpan.setDesc(desc);
			yanpan.setArea_id(area_id);
			yanpan.setUser_id(new Integer(user_id));
			yanpan = yanPanService.saveYanpan(yanpan);

			// 再落地文件表数据
			if (yanpan != null) {
				yanPanService.saveYanpanFiles(yanpan.getId(),pathList);
			}
			tempMap.put("code", Constants.SUCCESS);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "提交研判宝数据失败");
			Logger.error("提交研判宝数据失败" + tempMap + e);
		}
		return tempMap;
	}
	

	/* 获取保健宝或研判宝项目数据列表，可根据状态筛选 */
	@RequestMapping(value = "/programList/{userId}/{type}/{status}/{limit}/{offset}", method = RequestMethod.GET)
	public Map<String, Object> getProgramList(HttpServletRequest req,
			@PathVariable String userId,
			@PathVariable String type,
			@PathVariable String status,
			@PathVariable int limit,
			@PathVariable int offset
			) throws Exception {
		Logger.info("获取个人项目所有数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			if(userId==null||type==null){
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "userId或者type不可为空");
			}
			tempMap = yanPanService.getProgramList(userId,type,status,limit,offset);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取个人项目数据失败");
			Logger.error("获取个人项目数据失败" + tempMap);
		}
		return tempMap;
	}
	
	
	
	/* 获取用户是否存在未读接口 */
	@RequestMapping(value = "/unread/{userId}/{type}", method = RequestMethod.GET)
	public Map<String, Object> isProgramUnRead(HttpServletRequest req,
			@PathVariable String userId,
			@PathVariable String type
			) throws Exception {
		Logger.info("获取用户未读项目接口开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap = yanPanService.isUnRead(userId,type);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取用户未读项目接口失败");
			Logger.error("获取用户未读项目接口失败" + tempMap);
		}
		return tempMap;
	}
	
	/* 更新用户项目状态 */
	@RequestMapping(value = "/read", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> updateProgramReadStatus(HttpServletRequest req) throws Exception {
		Logger.info("更新用户项目状态接口开始");
		String type = (String) req.getParameter("type");
		String id = (String) req.getParameter("id");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			yanPanService.updateProgramReadStatus(type,id);
			tempMap.put("code", Constants.SUCCESS);
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "更新用户项目状态接口失败");
			Logger.error("更新用户项目状态接口失败" + tempMap);
		}
		return tempMap;
	}
	
	/* 获取研判宝具体某个项目详情 */
	@RequestMapping(value = "/yanpanProgram/{id}", method = RequestMethod.GET)
	public Map<String, Object> getProgramList(HttpServletRequest req,
			@PathVariable String id
			) throws Exception {
		Logger.info("获取研判宝具体项目数据开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			if(id==null){
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "研判主键不可为空");
			}
			tempMap = yanPanService.getYanPanProgram(id);

		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取个人项目数据失败");
			Logger.error("获取个人项目数据失败" + tempMap);
		}
		return tempMap;
	}
	
}
