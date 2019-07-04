package com.iot.smart_city.message.task;

import java.util.List;

import com.iot.smart_city.message.client.ScManageClient;
import com.iot.smart_city.message.service.ScMessageService;
import com.iot.smart_city.model.message.ScMessage;
import com.iot.smart_city.model.message.status.ScMessageStatus;
import com.smart_city.common.message.request.ResendMessageRequest;

/**
 * 消息系统定时任务:处理状态为发送中状态的超时消息
 * 
 * @author chrilwe
 *
 */
public class ScMessageTask2 implements Runnable {

	@Override
	public void run() {
		this.handleSendingMessage();
		try {
			Thread.sleep(2 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void handleSendingMessage() {
		// 分页查询超时未完成的任务
		SpringBeanUtil bean = new SpringBeanUtil();
		ScMessageService scMessageService = bean.getBean(ScMessageService.class);
		ScManageClient scManageClient = bean.getBean(ScManageClient.class);
		int total = scMessageService.countByStatus(ScMessageStatus.SENDING);
		if (total == 0) {
			return;
		}
		int size = 100;
		int totalPage = (total % size == 0) ? (total / size) : (total / size + 1);
		for (int i = 0; i < totalPage; i++) {
			int currentPage = i;
			if (currentPage <= 0) {
				currentPage = 1;
			}
			List<ScMessage> findList = scMessageService.findList((currentPage - 1) * size, size,
					ScMessageStatus.SENDING);
			if(findList != null) {
				//重新发送消息
				for(ScMessage scMessage : findList) {
					ResendMessageRequest resendMessageRequest = new ResendMessageRequest();
					resendMessageRequest.setMessageId(scMessage.getMessageId());
					scMessageService.resendMessage(resendMessageRequest);
				}
			}
		}
	}
}
