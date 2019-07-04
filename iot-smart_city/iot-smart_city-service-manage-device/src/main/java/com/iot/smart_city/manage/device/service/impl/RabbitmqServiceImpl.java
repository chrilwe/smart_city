package com.iot.smart_city.manage.device.service.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.manage.device.client.ScMessageClient;
import com.iot.smart_city.manage.device.client.ScSearchClient;
import com.iot.smart_city.manage.device.service.RabbitmqService;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.message.ScMessage;
import com.iot.smart_city.model.product.ScProduct;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.product.response.code.ScProductCode;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.response.code.ScSearchCode;
import com.smart_city.common.search.response.msg.ScSearchMsg;

@Service
public class RabbitmqServiceImpl implements RabbitmqService {

	@Autowired
	Channel channel;
	@Autowired
	ScMessageClient scMessageClient;
	@Autowired
	ScSearchClient scSearchClient;

	/**
	 * 添加product分布式事务消息
	 */
	@Override
	public ScBaseResponse subscribeProductAdd(String exchange, String queue, String routKey) {
		try {
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			/**
			 *  * 参数明细：  * 1、队列名称  * 2、是否持久化  * 3、是否独占此队列  * 4、队列不用是否自动删除
			 *  * 5、参数  
			 */
			channel.queueDeclare(queue, true, false, false, null);
			/**
			 * 交换机绑定队列  * 参数明细  * 1、队列名称  * 2、交换机名称  * 3、路由key  
			 */
			channel.queueBind(queue, exchange, routKey);

			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String messageId = new String(body, "utf-8");
					ScMessage scMessage = scMessageClient.findMessageById(messageId);
					if (scMessage != null) {
						String messageBody = scMessage.getMessageBody();
						if (!StringUtils.isEmpty(messageBody)) {
							ScProduct scProduct = JSON.parseObject(messageBody, ScProduct.class);
							ScSearchProductAddRequest addProductRequest = new ScSearchProductAddRequest();
							addProductRequest.setScProduct(scProduct);
							scSearchClient.addProduct(addProductRequest);
							SendingSuccessRequest sendingSuccessRequest = new SendingSuccessRequest();
							sendingSuccessRequest.setMessageId(messageId);
							scMessageClient.sendingSuccess(sendingSuccessRequest);
						}
					}
				}
			};
			/**
			 *          * 监听队列String queue, boolean autoAck,Consumer callback
			 *          * 参数明细          * 1、队列名称
			 *          * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
			 * 为false则需要手动回复          * 3、消费消息的方法，消费者接收到消息后调用此方法          
			 */
			channel.basicConsume(queue, false, consumer);
			return new ScBaseResponse(ScSearchCode.SUCCESS, ScSearchMsg.SUCCESS, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ScBaseResponse subscribeDeviceAdd(String exchange, String queue, String routKey) {
		try {
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			/**
			 *  * 参数明细：  * 1、队列名称  * 2、是否持久化  * 3、是否独占此队列  * 4、队列不用是否自动删除
			 *  * 5、参数  
			 */
			channel.queueDeclare(queue, true, false, false, null);
			/**
			 * 交换机绑定队列  * 参数明细  * 1、队列名称  * 2、交换机名称  * 3、路由key  
			 */
			channel.queueBind(queue, exchange, routKey);

			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String messageId = new String(body, "utf-8");
					ScMessage scMessage = scMessageClient.findMessageById(messageId);
					if (scMessage != null) {
						String messageBody = scMessage.getMessageBody();
						if (!StringUtils.isEmpty(messageBody)) {
							ScDevice scDevice = JSON.parseObject(messageBody, ScDevice.class);
							ScSearchDeviceAddRequest addDeviceRequest = new ScSearchDeviceAddRequest();
							addDeviceRequest.setScDevice(scDevice);
							scSearchClient.addDevice(addDeviceRequest);
							SendingSuccessRequest sendingSuccessRequest = new SendingSuccessRequest();
							sendingSuccessRequest.setMessageId(messageId);
							scMessageClient.sendingSuccess(sendingSuccessRequest);
						}
					}
				}
			};
			/**
			 *          * 监听队列String queue, boolean autoAck,Consumer callback
			 *          * 参数明细          * 1、队列名称
			 *          * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
			 * 为false则需要手动回复          * 3、消费消息的方法，消费者接收到消息后调用此方法          
			 */
			channel.basicConsume(queue, false, consumer);
			return new ScBaseResponse(ScSearchCode.SUCCESS, ScSearchMsg.SUCCESS, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
