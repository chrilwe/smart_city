package com.iot.smart_city.search.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.iot.smart_city.search.dao.ScSearchDao;
import com.iot.smart_city.search.service.ScSearchService;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.ScSearchType;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.request.ScSearchRequest;
import com.smart_city.common.search.response.ScSearchResponse;
import com.smart_city.common.search.response.code.ScSearchCode;
import com.smart_city.common.search.response.msg.ScSearchMsg;


@Service
public class ScSearchServiceImpl implements ScSearchService {

	@Autowired
	private ScSearchDao scSearchDao;

	@Value("${eleasticsearch.index}")
	private String index;

	/**
	 * 添加产品
	 */
	@Override
	public ScBaseResponse addProduct(ScSearchProductAddRequest addProductRequest) {
		if (addProductRequest == null) {
			return new ScBaseResponse(ScSearchCode.PARAM_NULL, ScSearchMsg.PARAM_NULL, false);
		}
		ScProduct scProduct = addProductRequest.getScProduct();
		IndexRequest indexRequest = new IndexRequest(index, ScSearchType.PRODUCT, scProduct.getProductId());
		Map<String, Object> source = new HashMap<>();
		source.put("productName", scProduct.getProductName());
		source.put("nodeType", scProduct.getNodeType());
		source.put("isGateWay", scProduct.getIsGateWay());
		source.put("protocol", scProduct.getProtocol());
		source.put("connectType", scProduct.getConnectType());
		source.put("dataType", scProduct.getDataType());
		source.put("description", scProduct.getDescription());
		source.put("status", scProduct.getStatus());
		source.put("createTime", scProduct.getCreateTime());
		source.put("updateTime", scProduct.getUpdateTime());
		indexRequest.source(source);
		ScBaseResponse addOrUpdate = null;
		try {
			addOrUpdate = scSearchDao.addOrUpdate(indexRequest);
		} catch (IOException e) {
			return new ScBaseResponse(ScSearchCode.SYSTEM_ERROR, ScSearchMsg.SYSTEM_ERROR, false);
		}
		return addOrUpdate;
	}

	/**
	 * 添加设备
	 */
	@Override
	public ScBaseResponse addDevice(ScSearchDeviceAddRequest addDeviceRequest) {
		if (addDeviceRequest == null) {
			return new ScBaseResponse(ScSearchCode.PARAM_NULL, ScSearchMsg.PARAM_NULL, false);
		}
		ScDevice scDevice = addDeviceRequest.getScDevice();
		IndexRequest indexRequest = new IndexRequest(index, ScSearchType.DEVICE, scDevice.getDeviceId());
		Map<String, Object> source = new HashMap<>();
		source.put("deviceName", scDevice.getDeviceName());
		source.put("lastTime", scDevice.getLastTime());
		source.put("productId", scDevice.getProductId());
		source.put("remarks", scDevice.getRemarks());
		source.put("status", scDevice.getStatus());
		indexRequest.source(source);
		ScBaseResponse addOrUpdate = null;
		try {
			addOrUpdate = scSearchDao.addOrUpdate(indexRequest);
		} catch (IOException e) {
			return new ScBaseResponse(ScSearchCode.SYSTEM_ERROR, ScSearchMsg.SYSTEM_ERROR, false);
		}
		return addOrUpdate;
	}

	/**
	 * 关键词搜索
	 */
	@Override
	public ScSearchResponse search(ScSearchRequest searchRequest) {
		if (searchRequest == null) {
			return new ScSearchResponse(ScSearchCode.PARAM_NULL, ScSearchMsg.PARAM_NULL, false, null, 0l, 0l);
		}
		String keyword = searchRequest.getKeyword();
		String queryType = searchRequest.getQueryType();
		int currentPage = searchRequest.getCurrentPage();
		int pageSize = searchRequest.getPageSize();

		SearchRequest esSearchRequest = new SearchRequest(index);
		esSearchRequest.types(queryType);
		
		// 过滤源字段
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 创建布尔查询对象
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		if (queryType.equals(ScSearchType.PRODUCT)) {
			searchSourceBuilder.fetchSource(new String[] { "productName,description" }, new String[] {});
			// 根据关键字搜索
			if (StringUtils.isNotEmpty(keyword)) {
				MultiMatchQueryBuilder multiMatchQuery = QueryBuilders
						.multiMatchQuery(keyword, "productName", "description").minimumShouldMatch("70%")
						.field("productName", 10);
				booleanQueryBuilder.must(multiMatchQuery);
			}

		} else if (queryType.equals(ScSearchType.DEVICE)) {
			searchSourceBuilder.fetchSource(new String[] { "deviceName,remarks" }, new String[] {});
			// 根据关键字搜索
			if (StringUtils.isNotEmpty(keyword)) {
				MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, "deviceName", "remarks")
						.minimumShouldMatch("70%").field("deviceName", 10);
				booleanQueryBuilder.must(multiMatchQuery);
			}

		}
		searchSourceBuilder.query(booleanQueryBuilder);

		// 分页条件
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int from = (currentPage - 1) * pageSize;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(pageSize);

		// 组装条件,执行查询
		esSearchRequest.source(searchSourceBuilder);
		ScSearchResponse queryResponse = null;
		try {
			queryResponse = scSearchDao.query(esSearchRequest, queryType, Long.parseLong(pageSize + ""));
		} catch (IOException e) {
			return new ScSearchResponse(ScSearchCode.SYSTEM_ERROR, ScSearchMsg.SYSTEM_ERROR, false, null, 0l, 0l);

		}
		return queryResponse;
	}

}
