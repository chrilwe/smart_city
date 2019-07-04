package com.iot.smart_city.model.product;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 产品信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProduct implements Serializable {
	private static final long serialVersionUID = -660254156100427256L;
	String productId;
	String productName;//产品名称
	String nodeType;//节点类型 :设备和网关 数字代表:1,2
	String isGateWay;//是否接入网关 1:是，2：否
	String protocol;//网络协议:Modbus, OPC UA,ZigBee,BLE
	String connectType;//连接方式 : WiFi,蜂窝（2G/3G/4G）,以太网,LoRaWAN
	String dataType;
	String description;
	String status;//状态：开发中，已发布
	Date createTime;
	Date updateTime;
}
