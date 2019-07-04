package com.iot.smart_city.model.product;

import java.io.Serializable;

import com.iot.smart_city.model.device.ScDevice;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDevice implements Serializable {
	ScProduct scProduct;
	ScDevice scDevice;
}
