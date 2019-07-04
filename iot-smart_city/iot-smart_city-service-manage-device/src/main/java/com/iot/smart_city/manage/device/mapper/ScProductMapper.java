package com.iot.smart_city.manage.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iot.smart_city.model.product.ScProduct;

public interface ScProductMapper {
	public void scProductAdd(ScProduct scProduct);
	public void scProductDel(String productId);
	public void scProductUpdate(ScProduct scProduct);
	public List<ScProduct> scProductQuery(@Param("start")int start, @Param("size")int size);
	public ScProduct findById(String productId);
}
