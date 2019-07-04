package com.iot.smart_city.model.message;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScMessage implements Serializable {
	private String messageId;
	private String messageBody;//消息内容
	private String dataType;//消息内容数据类型
	private String status;//消息状态
	private int resendTimes;//消息重新发送次数
	private int timeout;//消息消费过期时间
	private String exchange;//交换机
	private String queue;//消息队列
	private String routKey;//路由key
	private Date createTime;//生成消息时间
	private Date updateTime;//更新消息时间
	private int version;
}
