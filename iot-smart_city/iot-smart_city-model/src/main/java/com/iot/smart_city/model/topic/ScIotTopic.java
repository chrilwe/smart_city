package com.iot.smart_city.model.topic;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 主题
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScIotTopic implements Serializable {
	private static final long serialVersionUID = 850777874641308526L;
	
	private String id;
	private String productId;
	private String type;
	private String topic;//消息类
	private String descrption;
	private String status;//订阅状态
	private String topicName;
}
