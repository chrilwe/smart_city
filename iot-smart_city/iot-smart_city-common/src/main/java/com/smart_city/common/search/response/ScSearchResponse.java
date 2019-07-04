package com.smart_city.common.search.response;

import java.util.List;

import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ProductDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScSearchResponse extends ScBaseResponse {
	private List<ProductDevice> productDevices;
	private long total;//总记录
	private long totalPage;//总页数
	private long pageSize;//页面大小
	public ScSearchResponse(int code, String message, boolean isSuccess,
			List<ProductDevice> productDevices, long total, long pageSize) {
		super(code, message, isSuccess);
		this.productDevices = productDevices;
		this.total = total;
		if(pageSize != 0l) {
			this.totalPage = (total % pageSize == 0)?(total / pageSize):(total / pageSize + 1);

		}
		this.pageSize = pageSize;
	}

}
