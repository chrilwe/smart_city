package com.iot.smart_city.message.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.message.client.ScManageClient;
import com.iot.smart_city.message.service.ScMessageService;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.message.ScMessage;
import com.iot.smart_city.model.message.status.ScMessageStatus;
import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.message.request.ResendMessageRequest;

/**
 * 消息系统定时任务:处理状态为待发送状态的超时消息
 * @author chrilwe
 *
 */
public class ScMessageTask1 implements Runnable {

	@Override
	public void run() {
		this.handleWaitingSendMessage();
		try {
			Thread.sleep(2 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理状态为待发送状态的超时消息
	 */
	private void handleWaitingSendMessage() {
		//分页查询超时未完成的任务
		SpringBeanUtil bean = new SpringBeanUtil();
		ScMessageService scMessageService = bean.getBean(ScMessageService.class);
		ScManageClient scManageClient = bean.getBean(ScManageClient.class);
		int total = scMessageService.countByStatus(ScMessageStatus.WAITING_SEND);
		if(total == 0) {
			return;
		}
		int size = 100;
		int totalPage = (total % size == 0)?(total / size):(total / size + 1);
		for(int i = 0; i <totalPage; i++) {
			int currentPage = i;
			if(currentPage <= 0) {
				currentPage = 1;
			}
			List<ScMessage> findList = scMessageService.findList((currentPage - 1)*size, size, ScMessageStatus.WAITING_SEND);
			if(findList != null) {
				//未完成的先判断业务代码是否已经执行,如果没有执行重新发送消息,否则将这条消息作废
				for(ScMessage scMessage : findList) {
					String messageBody = scMessage.getMessageBody();
					if(StringUtils.isNotEmpty(messageBody)) {
						try {
							ScProduct scProduct = JSON.parseObject(messageBody, ScProduct.class);
							String productId = scProduct.getProductId();
							ScProduct findProductById = scManageClient.findProductById(productId);
							if(findProductById == null) {
								scMessage.setUpdateTime(new Date());
								scMessage.setStatus(ScMessageStatus.CANCEL);
								scMessageService.updateMessage(scMessage);
							} else {
								ResendMessageRequest resendMessageRequest = new ResendMessageRequest();
								resendMessageRequest.setMessageId(scMessage.getMessageId());
								scMessageService.resendMessage(resendMessageRequest);
							}
						} catch (Exception e) {
							ScDevice scDevice = JSON.parseObject(messageBody, ScDevice.class);
							String deviceId = scDevice.getDeviceId();
							ScDevice findDeviceById = scManageClient.findDeviceById(deviceId);
							if(findDeviceById == null) {
								scMessage.setUpdateTime(new Date());
								scMessage.setStatus(ScMessageStatus.CANCEL);
								scMessageService.updateMessage(scMessage);
							} else {
								ResendMessageRequest resendMessageRequest = new ResendMessageRequest();
								resendMessageRequest.setMessageId(scMessage.getMessageId());
								scMessageService.resendMessage(resendMessageRequest);
							}
						}
					}
					
				}
			}
		}
	}
	
}
