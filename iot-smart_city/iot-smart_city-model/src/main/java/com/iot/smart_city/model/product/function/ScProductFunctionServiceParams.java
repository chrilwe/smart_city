package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 产品功能服务参数
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunctionServiceParams implements Serializable {
	private static final long serialVersionUID = 2475368911836034855L;
	
	private String id;
	private String serviceId;//功能服务id
	private String name;
	private String type;//输出参数或者输入参数
	private String UDID;//标识符
	private String dataType;//数据类型
	private String isOpen;//开关：1,0
	private int start;//数值计算开始值
	private int end;//数值计算最大值
	private int unit;//数值计算单位值
	private String unitName;//单位名称
}
