package com.smart_city.common.filesystem.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class UploadFileResponse extends ScBaseResponse {
	String filePath;//请求地址
	public UploadFileResponse(int code, String message, boolean isSuccess, String filePath) {
		super(code, message, isSuccess);
		this.filePath = filePath;
	}

}
