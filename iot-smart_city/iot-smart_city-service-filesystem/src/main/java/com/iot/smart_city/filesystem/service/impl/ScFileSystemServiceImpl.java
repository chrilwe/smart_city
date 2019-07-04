package com.iot.smart_city.filesystem.service.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.smart_city.filesystem.service.ScFileSystemService;
import com.smart_city.common.filesystem.response.UploadFileResponse;
import com.smart_city.common.filesystem.response.code.ScFileSystemCode;
import com.smart_city.common.filesystem.response.msg.ScFileSystemMsg;

@Service
public class ScFileSystemServiceImpl implements ScFileSystemService {
	
	@Value("${filesystem.upload_file_size}")
	private long fileSize;//文件大小限制
	@Value("${filesystem.charset}")
	private String charset;
	@Value("${filesystem.connect_timeout_in_seconds}")
	private int connect_timeout_in_seconds;
	@Value("${filesystem.network_timeout_in_seconds}")
	private int network_timeout_in_seconds;

	@Override
	public UploadFileResponse uploadFile(MultipartFile file) {
		if(file == null) {
			return new UploadFileResponse(ScFileSystemCode.FILE_UPLOAD_NULL,ScFileSystemMsg.FILE_UPLOAD_NULL,false,null);
		}
		//判断文件是否超过限定大小/kb
		long size = file.getSize();
		if(size > fileSize) {
			return new UploadFileResponse(ScFileSystemCode.FILE_SIZE_LIMIT,ScFileSystemMsg.FILE_SIZE_LIMIT,false,null);
		}
		
		//上传文件
		String filePath = "";
		try {
			filePath = this.upload(file);
			if(StringUtils.isEmpty(filePath)) {
				return new UploadFileResponse(ScFileSystemCode.FILE_UPLOAD_FAIL,ScFileSystemMsg.FILE_UPLOAD_FAIL,false,null);
			}
			
			//TODO 文件信息入库
		} catch (Exception e) {
			return new UploadFileResponse(ScFileSystemCode.SYSTEM_ERROR,ScFileSystemMsg.SYSTEM_ERROR,false,null);
		}
		return new UploadFileResponse(ScFileSystemCode.SUCCESS,ScFileSystemMsg.SUCCESS,true,filePath);
	}
	
	private String upload(MultipartFile file) throws Exception {
		//文件服务器连接初始化
		Resource resource = new ClassPathResource("FastDfs.conf");
		ClientGlobal.init(resource + "FastDfs.conf");
		ClientGlobal.setG_charset(charset);
		ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
		ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
		
		//创建TrackerClient
		TrackerClient client = new TrackerClient();
		//获取trackerserver
		TrackerServer trackerServer = client.getConnection();
		//获取storage
		StorageServer storeStorage = client.getStoreStorage(trackerServer);
		//创建storageclient
		StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
		
		//上传文件
		byte[] file_buff = file.getBytes();
		String originalFilename = file.getOriginalFilename();
		int indexOf = originalFilename.indexOf(".");
		String file_ext_name = originalFilename.substring(indexOf + 1);
		String filePath = storageClient.upload_file1(file_buff, file_ext_name, null);
		return filePath;
	}

}
