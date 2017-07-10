package com.gyc.file.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gyc.dto.user;

public interface FileService {

	public Map<String, Object> FileUpload(MultipartFile file) throws Exception;

}
