package com.iot.smart_city.manage.device.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.manage.device.client.ScMessageClient;
import com.iot.smart_city.manage.device.mapper.ScProductMapper;
import com.iot.smart_city.manage.device.service.ScProductService;
import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.base.ScBaseRequest;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;
import com.smart_city.common.product.request.ScProductAddRequest;
import com.smart_city.common.product.request.ScProductDelRequest;
import com.smart_city.common.product.request.ScProductQueryRequest;
import com.smart_city.common.product.request.ScProductUpdateRequest;
import com.smart_city.common.product.response.ScProductAddResponse;
import com.smart_city.common.product.response.ScProductDelResponse;
import com.smart_city.common.product.response.ScProductQueryResponse;
import com.smart_city.common.product.response.ScProductUpdateResponse;
import com.smart_city.common.product.response.code.ScProductCode;
import com.smart_city.common.product.response.msg.ScProductMsg;
import com.smart_city.common.status.ScProductStatus;
import com.smart_city.common.type.DataType;
import com.smart_city.common.type.InetConnectType;
import com.smart_city.common.type.IsGateway;
import com.smart_city.common.type.NodeType;
import com.smart_city.common.type.ProtocolType;
/**
 * 产品管理
 * @author chrilwe
 *
 */
@Service
public class ScProductServiceImpl implements ScProductService {
	
	@Autowired
	private ScProductMapper scProductMapper;
	@Autowired
	private ScMessageClient scMessageClient;
	
	@Value("${message.timeout}")
	private int timeout;

	@Override
	@Transactional
	public ScProductAddResponse scProductAdd(ScProductAddRequest scProductAddRequest) {
		if(scProductAddRequest == null) {
			return new ScProductAddResponse(ScProductCode.PARAM_NULL,ScProductMsg.PARAM_NULL,false);
		}
		ScProduct scProduct = scProductAddRequest.getScProduct();
		String productId = UUID.randomUUID().toString();
		scProduct.setProductId(productId);
		scProduct.setCreateTime(new Date());
		scProduct.setStatus(ScProductStatus.COODING);
		
		String connectType = scProduct.getConnectType();
		String dataType = scProduct.getDataType();
		String isGateWay = scProduct.getIsGateWay();
		String nodeType = scProduct.getNodeType();
		String productName = scProduct.getProductName();
		String protocol = scProduct.getProtocol();
		if(StringUtils.isEmpty(connectType) || StringUtils.isEmpty(dataType) 
				|| StringUtils.isEmpty(isGateWay) || StringUtils.isEmpty(connectType)
				|| StringUtils.isEmpty(nodeType) || StringUtils.isEmpty(productName)
				|| StringUtils.isEmpty(protocol)) {
			return new ScProductAddResponse(ScProductCode.PARAM_NOT_ALLOW_NULL,ScProductMsg.PARAM_NOT_ALLOW_NULL,false);
		}
		
		if((!isGateWay.equals(IsGateway.YES) && !isGateWay.equals(IsGateway.NO))
				|| (!nodeType.equals(NodeType.DEVICE) && !nodeType.equals(NodeType.GATEWAY))
				|| (!connectType.equals(InetConnectType.WIFI) && !connectType.equals(InetConnectType.ETHERNET)
				&& !connectType.equals(InetConnectType.GPRS) && !connectType.equals(InetConnectType.LORAWAN))
				|| (!protocol.equals(ProtocolType.BLE) && !protocol.equals(ProtocolType.MODBUS)
						&& !protocol.equals(ProtocolType.OPCUA) && !protocol.equals(ProtocolType.ZIGBEE))) {
			return new ScProductAddResponse(ScProductCode.ERROR_PARAM,ScProductMsg.ERROR_PARAM,false);
		}
		
		//添加产品信息到数据库和elastaticsearch中
		String messageId = UUID.randomUUID().toString();
		WaitingSendMessageRequest waitingSendMessageRequest = new WaitingSendMessageRequest();
		waitingSendMessageRequest.setDataType(DataType.JSON);
		waitingSendMessageRequest.setExchange("product_exchange");
		waitingSendMessageRequest.setMessageBody(JSON.toJSONString(scProduct));
		waitingSendMessageRequest.setMessageId(messageId);
		waitingSendMessageRequest.setQueue("product_queue");
		waitingSendMessageRequest.setRoutKey("product");
		waitingSendMessageRequest.setTimeout(timeout);
		scMessageClient.waitingSendMessage(waitingSendMessageRequest);
		scProductMapper.scProductAdd(scProduct);
		SendingMessageRequest sendingMessageRequest = new SendingMessageRequest();
		sendingMessageRequest.setMessageId(messageId);
		scMessageClient.sendingMessage(sendingMessageRequest);
		return new ScProductAddResponse(ScProductCode.SUCCESS,ScProductMsg.SUCCESS,true);
	}

	@Override
	@Transactional
	public ScProductDelResponse scProductDel(ScProductDelRequest scProductDelRequest) {
		if(scProductDelRequest == null) {
			return new ScProductDelResponse(ScProductCode.PARAM_NULL,ScProductMsg.PARAM_NULL,false);
		}
		String productId = scProductDelRequest.getProductId();
		scProductMapper.scProductDel(productId);
		return new ScProductDelResponse(ScProductCode.SUCCESS,ScProductMsg.SUCCESS,true);
	}

	@Override
	@Transactional
	public ScProductUpdateResponse scProductUpdate(ScProductUpdateRequest scProductUpdateRequest) {
		if(scProductUpdateRequest == null) {
			return new ScProductUpdateResponse(ScProductCode.PARAM_NULL,ScProductMsg.PARAM_NULL,false);
		}
		
		ScProduct scProduct = scProductUpdateRequest.getScProduct();
		String connectType = scProduct.getConnectType();
		String dataType = scProduct.getDataType();
		String isGateWay = scProduct.getIsGateWay();
		String nodeType = scProduct.getNodeType();
		String productName = scProduct.getProductName();
		String protocol = scProduct.getProtocol();
		if(StringUtils.isEmpty(connectType) || StringUtils.isEmpty(dataType) 
				|| StringUtils.isEmpty(isGateWay) || StringUtils.isEmpty(connectType)
				|| StringUtils.isEmpty(nodeType) || StringUtils.isEmpty(productName)
				|| StringUtils.isEmpty(protocol)) {
			return new ScProductUpdateResponse(ScProductCode.PARAM_NOT_ALLOW_NULL,ScProductMsg.PARAM_NOT_ALLOW_NULL,false);
		}
		
		if((!isGateWay.equals(IsGateway.YES) && !isGateWay.equals(IsGateway.NO))
				|| (!nodeType.equals(NodeType.DEVICE) && !nodeType.equals(NodeType.GATEWAY))
				|| (!connectType.equals(InetConnectType.WIFI) && !connectType.equals(InetConnectType.ETHERNET)
				&& !connectType.equals(InetConnectType.GPRS) && !connectType.equals(InetConnectType.LORAWAN))
				|| (!protocol.equals(ProtocolType.BLE) && !protocol.equals(ProtocolType.MODBUS)
						&& !protocol.equals(ProtocolType.OPCUA) && !protocol.equals(ProtocolType.ZIGBEE))) {
			return new ScProductUpdateResponse(ScProductCode.ERROR_PARAM,ScProductMsg.ERROR_PARAM,false);
		}
		
		ScProduct findById = scProductMapper.findById(scProduct.getProductId());
		if(findById == null) {
			return new ScProductUpdateResponse(ScProductCode.PRODUCT_NOT_FOUND,ScProductMsg.PRODUCT_NOT_FOUND,false);
		}
		scProduct.setUpdateTime(new Date());
		scProductMapper.scProductUpdate(scProduct);
		
		return new ScProductUpdateResponse(ScProductCode.SUCCESS,ScProductMsg.SUCCESS,true);
	}

	@Override
	public ScProductQueryResponse scProductQuery(ScProductQueryRequest scProductQueryRequest) {
		
		return null;
	}

	@Override
	public ScProduct findById(String productId) {
		if(StringUtils.isEmpty(productId)) {
			return null;
		}
		return scProductMapper.findById(productId);
	}

}
