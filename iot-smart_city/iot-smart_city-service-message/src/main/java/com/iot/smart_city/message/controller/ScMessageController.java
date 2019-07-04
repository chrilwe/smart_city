package com.iot.smart_city.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScMessageApi;
import com.iot.smart_city.message.service.ScMessageService;
import com.iot.smart_city.model.message.ScMessage;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;

@RestController
@RequestMapping("/message")
public class ScMessageController implements ScMessageApi {
	
	@Autowired
	private ScMessageService scMessageService;
	
	/**
	 * 预发送消息
	 */
	@Override
	@PostMapping("/waitingSendMessage")
	public ScBaseResponse waitingSendMessage(WaitingSendMessageRequest waitingSendMessageRequest) {
		ScBaseResponse waitingSendMessage = scMessageService.waitingSendMessage(waitingSendMessageRequest);
		return waitingSendMessage;
	}
	
	/**
	 * 消息发送中
	 */
	@Override
	@GetMapping("/sendingMessage")
	public ScBaseResponse sendingMessage(SendingMessageRequest sendingMessageRequest) {
		ScBaseResponse sendingMessage = scMessageService.sendingMessage(sendingMessageRequest);
		return sendingMessage;
	}
	
	/**
	 * 消息发送成功
	 */
	@Override
	@GetMapping("/sendingSuccess")
	public ScBaseResponse sendingSuccess(SendingSuccessRequest sendingSuccessRequest) {
		ScBaseResponse sendingSuccess = scMessageService.sendingSuccess(sendingSuccessRequest);
		return sendingSuccess;
	}
	
	/**
	 * 根据消息id查询
	 */
	@Override
	@GetMapping("/find")
	public ScMessage findMessageById(@RequestParam("messageId")String messageId) {
		ScMessage findMessageById = scMessageService.findMessageById(messageId);
		return findMessageById;
	}

}
