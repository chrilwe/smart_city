package com.iot.smart_city.manage.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScDeviceApi;
import com.iot.smart_city.manage.device.service.ScDeviceService;
import com.iot.smart_city.model.device.ScDevice;
import com.smart_city.common.base.ScBaseController;
import com.smart_city.common.device.request.ScDeviceAddRequest;
import com.smart_city.common.device.request.ScDeviceDelRequest;
import com.smart_city.common.device.request.ScDeviceQueryRequest;
import com.smart_city.common.device.request.ScDeviceUpdateRequest;
import com.smart_city.common.device.response.ScDeviceAddResponse;
import com.smart_city.common.device.response.ScDeviceDelResponse;
import com.smart_city.common.device.response.ScDeviceQueryResponse;
import com.smart_city.common.device.response.ScDeviceUpdateResponse;
import com.smart_city.common.device.response.code.ScDeviceCode;
import com.smart_city.common.device.response.msg.ScDeviceMsg;
import com.smart_city.common.product.response.ScProductAddResponse;
import com.smart_city.common.product.response.code.ScProductCode;
import com.smart_city.common.product.response.msg.ScProductMsg;
/**
 * 智慧城市设备管理
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/manage/device")
public class ScDeviceController extends ScBaseController implements ScDeviceApi {
	
	@Autowired
	ScDeviceService scDeviceService;
	
	/**
	 * 添加设备
	 */
	@Override
	@PostMapping("/add")
	public ScDeviceAddResponse scDeviceAdd(ScDeviceAddRequest scDeviceRequest) {
		ScDeviceAddResponse scDeviceAdd = null;
		try {
			scDeviceAdd = scDeviceService.scDeviceAdd(scDeviceRequest);
		} catch (Exception e) {
			return new ScDeviceAddResponse(ScDeviceCode.SYSTEM_ERROR,
					ScDeviceMsg.SYSTEM_ERROR,false,null,null);
		}
		return scDeviceAdd;
	}
	
	/**
	 * 删除设备
	 */
	@Override
	@GetMapping("/delete")
	public ScDeviceDelResponse scDeviceDel(ScDeviceDelRequest scDeviceDelRequest) {
		ScDeviceDelResponse scDeviceDel = null;
		try {
			scDeviceDel = scDeviceService.scDeviceDel(scDeviceDelRequest);
		} catch (Exception e) {
			return new ScDeviceDelResponse(ScDeviceCode.SYSTEM_ERROR,
					ScDeviceMsg.SYSTEM_ERROR,false);
		}
		
		return scDeviceDel;
	}
	
	/**
	 * 更新设备
	 */
	@Override
	@PostMapping("/update")
	public ScDeviceUpdateResponse scDeviceUpdate(ScDeviceUpdateRequest scDeviceUpdateRequest) {
		ScDeviceUpdateResponse scDeviceUpdate = null;
		try {
			scDeviceUpdate = scDeviceService.scDeviceUpdate(scDeviceUpdateRequest);
		} catch (Exception e) {
			return new ScDeviceUpdateResponse(ScDeviceCode.SYSTEM_ERROR,
					ScDeviceMsg.SYSTEM_ERROR,false,null,null);
		}
		return scDeviceUpdate;
	}
	
	/**
	 * 查询设备
	 */
	@Override
	@GetMapping("/query")
	public ScDeviceQueryResponse scDeviceQuery(ScDeviceQueryRequest scDeviceQueryRequest) {
		ScDeviceQueryResponse scDeviceQuery = null;
		try {
			scDeviceQuery = scDeviceService.scDeviceQuery(scDeviceQueryRequest);
		} catch (Exception e) {
			return new ScDeviceQueryResponse(ScDeviceCode.SYSTEM_ERROR,
					ScDeviceMsg.SYSTEM_ERROR,false,null);
		}
		return scDeviceQuery;
	}

	@Override
	@GetMapping("/find")
	public ScDevice findDeviceById(String id) {
		
		return scDeviceService.findDeviceById(id);
	}

}
