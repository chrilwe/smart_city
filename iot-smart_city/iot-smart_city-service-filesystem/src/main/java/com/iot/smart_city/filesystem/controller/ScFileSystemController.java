package com.iot.smart_city.filesystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iot.smart_city.api.ScFileSystemApi;
import com.smart_city.common.filesystem.response.UploadFileResponse;
/**
 * 文件管理系统
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/filesystem")
public class ScFileSystemController implements ScFileSystemApi {
	
	/**上传文件**/
	@Override
	@PostMapping("/upload")
	public UploadFileResponse uploadFile(MultipartFile file) {
		
		return null;
	}

}
