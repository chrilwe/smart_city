package com.iot.smart_city.message.service;

import java.util.List;

import com.iot.smart_city.model.message.ScMessage;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.ResendMessageRequest;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;

public interface ScMessageService {
	public ScBaseResponse waitingSendMessage(WaitingSendMessageRequest waitingSendMessageRequest);//预发送消息
	public ScBaseResponse sendingMessage(SendingMessageRequest sendingMessageRequest);//发送消息中
	public ScBaseResponse sendingSuccess(SendingSuccessRequest sendingSuccessRequest);//确定完成消息发送
	public ScBaseResponse resendMessage(ResendMessageRequest resendMessageRequest);//重新发送消息
	public ScMessage findMessageById(String messageId);
	public int countByStatus(String status);//计算当前状态的总记录
	public List<ScMessage> findList(int start,int size,String status);//分页查询当前状态的消息
	public void updateMessage(ScMessage scMessage);
}
