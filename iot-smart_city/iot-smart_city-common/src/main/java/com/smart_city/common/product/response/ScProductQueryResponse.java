package com.smart_city.common.product.response;

import java.util.List;

import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ProductDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScProductQueryResponse extends ScBaseResponse {
	List<ProductDevice> productInfos;
	int totalPage;
	int totalCount;
	int pageSize;
	public ScProductQueryResponse(int code, String message, 
			boolean isSuccess,List<ProductDevice> productInfos,int totalCount,int pageSize) {
		super(code, message, isSuccess);
		this.productInfos = productInfos;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.totalPage = (totalCount%pageSize == 0)?(totalCount/pageSize):(totalCount/pageSize+1);
	}

}
