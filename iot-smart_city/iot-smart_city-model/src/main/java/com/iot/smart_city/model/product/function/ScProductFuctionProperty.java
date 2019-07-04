package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 产品功能属性
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFuctionProperty implements Serializable {
	private static final long serialVersionUID = 6616639928483911176L;
	
	private String propertyId;
	private String productId;//产品id
	private String type;//属性所属类型
	private String propertyName;//属性名称
	private String properyUDID;//标识符
	private String dataType;//数据类型 :布尔类型 整型类型
	private String isOpen;//开关：1,0
	private int start;//数值计算开始值
	private int end;//数值计算最大值
	private int unit;//数值计算单位值
	private String unitName;//单位名称
	private String readOrWrite;//是否只读
	private String description;//描述
	
}
