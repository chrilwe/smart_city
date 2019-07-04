package com.iot.smart_city.message.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.smart_city.message.mapper.ScMessageMapper;
import com.iot.smart_city.message.service.RabbitmqService;
import com.iot.smart_city.message.service.ScMessageService;
import com.iot.smart_city.model.message.ScMessage;
import com.iot.smart_city.model.message.status.ScMessageStatus;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.ResendMessageRequest;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;
import com.smart_city.common.message.response.code.ScMessageCode;
import com.smart_city.common.message.response.msg.ScMessageMsg;
@Service
public class ScMessageServiceImpl implements ScMessageService {
	
	@Autowired
	private ScMessageMapper scMessageMapper;
	@Autowired
	private RabbitmqService rabbitmqService;
	
	@Value("${message.maxResendTimes}")
	private int maxResendTimes;

	@Override
	@Transactional
	public ScBaseResponse waitingSendMessage(WaitingSendMessageRequest waitingSendMessageRequest) {
		if(waitingSendMessageRequest == null) {
			return new ScBaseResponse(ScMessageCode.PARAM_NULL,ScMessageMsg.PARAM_NULL,false);
		}
		
		String dataType = waitingSendMessageRequest.getDataType();
		String exchange = waitingSendMessageRequest.getExchange();
		String messageBody = waitingSendMessageRequest.getMessageBody();
		String queue = waitingSendMessageRequest.getQueue();
		String routKey = waitingSendMessageRequest.getRoutKey();
		int timeout = waitingSendMessageRequest.getTimeout();
		String messageId = waitingSendMessageRequest.getMessageId();
		if(StringUtils.isEmpty(messageId) || StringUtils.isEmpty(exchange) 
				|| StringUtils.isEmpty(messageBody) || StringUtils.isEmpty(queue)
				|| StringUtils.isEmpty(routKey) || StringUtils.isEmpty(dataType)) {
			return new ScBaseResponse(ScMessageCode.PARAM_ISNOTALLOW_NULL,ScMessageMsg.PARAM_ISNOTALLOW_NULL,false);
		}
		//确保重复发送添加消息的幂等性
		ScMessage findById = scMessageMapper.findById(messageId);
		if(findById != null) {
			return new ScBaseResponse(ScMessageCode.MESSAGE_EXIST,ScMessageMsg.MESSAGE_EXIST,false);
		}
		
		ScMessage scMessage = new ScMessage();
		scMessage.setCreateTime(new Date());
		scMessage.setDataType(dataType);
		scMessage.setExchange(exchange);
		scMessage.setMessageBody(messageBody);
		scMessage.setMessageId(messageId);
		scMessage.setQueue(queue);
		scMessage.setResendTimes(0);
		scMessage.setRoutKey(routKey);
		scMessage.setStatus(ScMessageStatus.WAITING_SEND);
		scMessage.setTimeout(timeout);
		scMessage.setVersion(1);
		return new ScBaseResponse(ScMessageCode.SUCCESS,ScMessageMsg.SUCCESS,true);
	}

	@Override
	@Transactional
	public ScBaseResponse sendingMessage(SendingMessageRequest sendingMessageRequest) {
		if(sendingMessageRequest == null) {
			return new ScBaseResponse(ScMessageCode.PARAM_NULL,ScMessageMsg.PARAM_NULL,false);
		}
		String messageId = sendingMessageRequest.getMessageId();
		ScMessage findById = scMessageMapper.findById(messageId);
		if(findById == null) {
			return new ScBaseResponse(ScMessageCode.MESSAGE_NO_EXIST,ScMessageMsg.MESSAGE_NO_EXIST,false);
		}
		String exchange = findById.getExchange();
		String queue = findById.getQueue();
		String routKey = findById.getRoutKey();
		String messageBody = findById.getMessageBody();
		
		//将消息状态变更为发送中
		findById.setUpdateTime(new Date());
		findById.setStatus(ScMessageStatus.SENDING);
		scMessageMapper.update(findById);
		
		//发送消息到 rabbitmq
		ScBaseResponse sendMessage = rabbitmqService.sendMessage(messageId, exchange, queue, routKey);
		return new ScBaseResponse(ScMessageCode.SUCCESS,ScMessageMsg.SUCCESS,true);
	}

	@Override
	@Transactional
	public ScBaseResponse sendingSuccess(SendingSuccessRequest sendingSuccessRequest) {
		if(sendingSuccessRequest == null) {
			return new ScBaseResponse(ScMessageCode.PARAM_NULL,ScMessageMsg.PARAM_NULL,false);
		}
		String messageId = sendingSuccessRequest.getMessageId();
		ScMessage findById = scMessageMapper.findById(messageId);
		if(findById == null) {
			return new ScBaseResponse(ScMessageCode.MESSAGE_NO_EXIST,ScMessageMsg.MESSAGE_NO_EXIST,false);
		}
		String status = findById.getStatus();
		//消息状态为发送中状态 才能更新为发送成功
		if(!status.equals(ScMessageStatus.SENDING)) {
			return new ScBaseResponse(ScMessageCode.MESSAGE_STATUS_ISNOT_SENDING,ScMessageMsg.MESSAGE_STATUS_ISNOT_SENDING,false);
		}
		findById.setUpdateTime(new Date());
		findById.setStatus(ScMessageStatus.SEND_SUCCESS);
		scMessageMapper.update(findById);
		return new ScBaseResponse(ScMessageCode.SUCCESS,ScMessageMsg.SUCCESS,true);
	}

	@Override
	@Transactional
	public ScBaseResponse resendMessage(ResendMessageRequest resendMessageRequest) {
		if(resendMessageRequest == null) {
			return new ScBaseResponse(ScMessageCode.PARAM_NULL,ScMessageMsg.PARAM_NULL,false);
		}
		String messageId = resendMessageRequest.getMessageId();
		//判断重发次数是否超过限定次数，如果超过重发次数，将状态修改为等待人工处理
		ScMessage findById = scMessageMapper.findById(messageId);
		int resendTimes = findById.getResendTimes();
		String status = findById.getStatus();
		String messageBody = findById.getMessageBody();
		String exchange = findById.getExchange();
		String queue = findById.getQueue();
		String routKey = findById.getRoutKey();
		if(resendTimes >= maxResendTimes) {
			if(status.equals(ScMessageStatus.WAITING_SEND)) {
				findById.setStatus(ScMessageStatus.WAITING_PEOPLE_HANDLE);
				findById.setUpdateTime(new Date());
				scMessageMapper.update(findById);
				//TODO 通知管理员消息异常
			}
			return new ScBaseResponse(ScMessageCode.MESSAGE_SENDTIMES_LIMIT,ScMessageMsg.MESSAGE_SENDTIMES_LIMIT,false);
		}
		int times = resendTimes + 1;
		scMessageMapper.updateResendTimes(messageId, resendTimes, new Date(),
				ScMessageStatus.SENDING, findById.getVersion());
		//发送消息到 rabbitmq
		ScBaseResponse sendMessage = rabbitmqService.sendMessage(messageId, exchange, queue, routKey);
		return new ScBaseResponse(ScMessageCode.SUCCESS,ScMessageMsg.SUCCESS,true);
	}

	@Override
	public ScMessage findMessageById(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			return null;
		}
		ScMessage findById = scMessageMapper.findById(messageId);
		return findById;
	}

	@Override
	public int countByStatus(String status) {
		int countByStatus = scMessageMapper.countByStatus(status);
		return countByStatus;
	}

	@Override
	public List<ScMessage> findList(int start, int size, String status) {
		List<ScMessage> findList = scMessageMapper.findList(start, size, status);
		return findList;
	}

	@Override
	@Transactional
	public void updateMessage(ScMessage scMessage) {
		scMessageMapper.update(scMessage);
	}

}
