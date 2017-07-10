package com.gyc.file.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gyc.common.Constants;
import com.gyc.file.service.FileService;
import com.gyc.user.mapper.UserMapper;

@Service
public class FileServiceImpl implements FileService {

	@Value("${file.src}")
	String filesrc;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Map<String, Object> FileUpload(MultipartFile file) throws Exception {

		// 设置文件上传路径
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String pathdir = filesrc;
		try {
			// 根据当前日期创建文件夹
			String foldername = new SimpleDateFormat("YYYYMMdd").format(System.currentTimeMillis());
			pathdir = pathdir + "/" + foldername;
			String returnpath = "files"+"/"+foldername;
			File tempfile = new File(pathdir);
			if (!tempfile.exists() && !tempfile.isDirectory()) {
				tempfile.mkdir();
			}

			byte[] bytes = file.getBytes();
			String filename = file.getOriginalFilename();
			String prefix = filename.substring(filename.lastIndexOf(".") + 1);
			String name = userMapper.getUUID();
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(pathdir, name + "." + prefix)));
			stream.write(bytes);
			stream.close();
			tempMap.put("code", Constants.SUCCESS);
			data.put("path", returnpath + "/" + name + "." + prefix);
			tempMap.put("data", data);
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.SUCCESS);
			tempMap.put("msg", Constants.SUCCESS+e);
			return tempMap;
		}

	}

}
