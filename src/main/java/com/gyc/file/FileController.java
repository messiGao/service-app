package com.gyc.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gyc.common.Constants;
import com.gyc.common.RedisUtils;
import com.gyc.file.service.FileService;
import com.gyc.user.mapper.UserMapper;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;

	private static final Logger Logger = LoggerFactory.getLogger(FileController.class);

	@RequestMapping(value = "/file", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file) {
		Logger.info("上传图片开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		if (!file.isEmpty()) {
			try {
				tempMap = fileService.FileUpload(file);
				Logger.info("上传图片结束");
				return tempMap;
			} catch (Exception e) {
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", "上传图片失败"+e);
				return tempMap;
			}
		} else {
			return tempMap;
		}
	}
}