package com.iot.smart_city.filesystem.service;

import org.springframework.web.multipart.MultipartFile;

import com.smart_city.common.filesystem.response.UploadFileResponse;

public interface ScFileSystemService {
	public UploadFileResponse uploadFile(MultipartFile file);
}
