package com.iot.smart_city.api;
/**
 * 文件管理
 * @author chrilwe
 *
 */

import org.springframework.web.multipart.MultipartFile;

import com.smart_city.common.filesystem.response.UploadFileResponse;

public interface ScFileSystemApi {
	//上传文件
	public UploadFileResponse uploadFile(MultipartFile file);
}
