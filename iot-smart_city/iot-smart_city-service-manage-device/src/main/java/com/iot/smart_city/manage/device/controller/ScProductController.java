package com.iot.smart_city.manage.device.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScProductApi;
import com.iot.smart_city.manage.device.service.ScProductService;
import com.iot.smart_city.model.product.ProductDevice;
import com.iot.smart_city.model.product.ScProduct;
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
/**
 * 智慧城市产品管理
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/manage/product")
public class ScProductController implements ScProductApi {
	
	@Autowired
	ScProductService scProductService;

	@Override
	@PostMapping("/add")
	public ScProductAddResponse scProductAdd(ScProductAddRequest scProductAddRequest) {
		ScProductAddResponse response = null;
		try {
			response = scProductService.scProductAdd(scProductAddRequest);
		} catch (Exception e) {
			return new ScProductAddResponse(ScProductCode.SYSTEM_ERROR,
					ScProductMsg.SYSTEM_ERROR,false);
		}
		return response;
	}

	@Override
	@GetMapping("/delete")
	public ScProductDelResponse scProductDel(ScProductDelRequest scProductDelRequest) {
		ScProductDelResponse response = null;
		try {
			response = scProductService.scProductDel(scProductDelRequest);
		} catch (Exception e) {
			return new ScProductDelResponse(ScProductCode.SYSTEM_ERROR,
					ScProductMsg.SYSTEM_ERROR,false);
		}
		return response;
	}

	@Override
	@PostMapping("/update")
	public ScProductUpdateResponse scProductUpdate(ScProductUpdateRequest scProductUpdateRequest) {
		ScProductUpdateResponse scProductUpdate = null;
		try {
			scProductUpdate = scProductService.scProductUpdate(scProductUpdateRequest);
		} catch (Exception e) {
			return new ScProductUpdateResponse(ScProductCode.SYSTEM_ERROR,
					ScProductMsg.SYSTEM_ERROR,false);
		}
		return scProductUpdate;
	}

	@Override
	@GetMapping("/query")
	public ScProductQueryResponse scProductQuery(ScProductQueryRequest scProductQueryRequest) {
		/*ScProductQueryResponse scProductQuery = null;
		try {
			scProductQuery = scProductService.scProductQuery(scProductQueryRequest);
		} catch (Exception e) {
			return null;
		}*/
		ScProduct product = new ScProduct();
		product.setProductId("1");
		product.setProductName("product01");
		ScProduct product1 = new ScProduct();
		product1.setProductId("1");
		product1.setProductName("product01");
		List<ProductDevice> list = new ArrayList<>();
		ProductDevice pd1 = new ProductDevice();
		pd1.setScProduct(product);
		ProductDevice pd = new ProductDevice();
		pd.setScProduct(product1);
		list.add(pd);
		list.add(pd1);
		ScProductQueryResponse scProductQuery = new ScProductQueryResponse(ScProductCode.SUCCESS,ScProductMsg.SUCCESS,true,list,100,100);
		return scProductQuery;
	}
	
	/**
	 * 根据id查询
	 */
	@Override
	@GetMapping("/find")
	public ScProduct findProductById(@RequestParam("productId")String productId) {
		
		return null;
	}

}
