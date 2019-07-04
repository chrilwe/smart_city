package com.iot.smart_city.manage.device.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.smart_city.model.message.ScMessage;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;
import com.smart_city.common.services.ScServiceList;

@FeignClient(ScServiceList.IOT_SMART_CITY_SERVICE_MESSAGE)
@RequestMapping("/message")
public interface ScMessageClient {
	/**
	 * 预发送消息
	 */
	@PostMapping("/waitingSendMessage")
	public ScBaseResponse waitingSendMessage(WaitingSendMessageRequest waitingSendMessageRequest);

	/**
	 * 消息发送中
	 */
	@GetMapping("/sendingMessage")
	public ScBaseResponse sendingMessage(SendingMessageRequest sendingMessageRequest);

	/**
	 * 根据消息id查询
	 */
	@GetMapping("/find")
	public ScMessage findMessageById(@RequestParam("messageId")String messageId);
	
	/**
	 * 消息发送成功
	 */
	@GetMapping("/sendingSuccess")
	public ScBaseResponse sendingSuccess(SendingSuccessRequest sendingSuccessRequest);
}
