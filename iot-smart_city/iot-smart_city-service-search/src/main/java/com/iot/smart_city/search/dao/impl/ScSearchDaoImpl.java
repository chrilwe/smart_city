package com.iot.smart_city.search.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ProductDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.iot.smart_city.search.dao.ScSearchDao;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.ScSearchType;
import com.smart_city.common.search.response.ScSearchResponse;
import com.smart_city.common.search.response.code.ScSearchCode;
import com.smart_city.common.search.response.msg.ScSearchMsg;

@Repository
public class ScSearchDaoImpl implements ScSearchDao {
	
	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	/**
	 * 更新或者添加文档
	 * @throws IOException 
	 */
	@Override
	public ScBaseResponse addOrUpdate(IndexRequest indexRequest) throws IOException {
		IndexResponse response = restHighLevelClient.index(indexRequest);
		RestStatus status = response.status();
		if(RestStatus.OK.getStatus() == status.getStatus()) {
			return new ScBaseResponse(ScSearchCode.SYSTEM_ERROR,ScSearchMsg.SYSTEM_ERROR,false);
		}
		return new ScBaseResponse(ScSearchCode.SUCCESS,ScSearchMsg.SUCCESS,true);
	}
	
	/**
	 * 删除文档
	 * @throws IOException 
	 */
	@Override
	public ScBaseResponse delete(DeleteRequest deleteRequest) throws IOException {
		DeleteResponse response = restHighLevelClient.delete(deleteRequest);
		if(response.status().getStatus() != RestStatus.OK.getStatus()) {
			return new ScBaseResponse(ScSearchCode.SYSTEM_ERROR,ScSearchMsg.SYSTEM_ERROR,false);
		}
		return new ScBaseResponse(ScSearchCode.SUCCESS,ScSearchMsg.SUCCESS,true);
	}
	
	/**
	 * 关键字查询
	 * @throws IOException 
	 */
	@Override
	public ScSearchResponse query(SearchRequest searchRequest,String searchType,long pageSize) throws IOException {
		SearchResponse response = restHighLevelClient.search(searchRequest);
		RestStatus status = response.status();
		if(status.getStatus() != RestStatus.OK.getStatus()) {
			return new ScSearchResponse(ScSearchCode.SYSTEM_ERROR,ScSearchMsg.SYSTEM_ERROR,false,null,0l,0l);
		}
		
		SearchHits hits = response.getHits();
		long total = hits.getTotalHits();
		List<ProductDevice> list = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSourceAsMap();
			ProductDevice pd = new ProductDevice();
			if(searchType.equals(ScSearchType.PRODUCT)) {
				String id = (String) source.get("id");
				String productName = (String) source.get("productName");
				String nodeType = (String) source.get("nodeType");
				String isGateWay = (String) source.get("isGateWay");
				String protocol = (String) source.get("protocol");
				String connectType = (String) source.get("connectType");
				String dataType =(String) source.get("dataType");
				String description = (String) source.get("description");
				String status1 = (String) source.get("status");
				Date createTime = (Date) source.get("createTime");
				Date updateTime = (Date) source.get("updateTime");
				ScProduct scProduct = new ScProduct();
				scProduct.setConnectType(connectType);
				scProduct.setCreateTime(createTime);
				scProduct.setDataType(dataType);
				scProduct.setDescription(description);
				scProduct.setIsGateWay(isGateWay);
				scProduct.setNodeType(nodeType);
				scProduct.setProductId(id);
				scProduct.setProductName(productName);
				scProduct.setProtocol(protocol);
				scProduct.setStatus(status1);
				scProduct.setUpdateTime(updateTime);
				pd.setScProduct(scProduct);
				list.add(pd);
				
			} else if(searchType.equals(ScSearchType.DEVICE)) {
				String deviceId = (String) source.get("id");
				String productId = (String) source.get("productId");
				String deviceName = (String) source.get("deviceName");
				String status2 = (String) source.get("status");
				String lastTime = (String) source.get("lastTime");
				String remarks = (String) source.get("remarks");
				
				ScDevice scDevice = new ScDevice();
				scDevice.setDeviceId(deviceId);
				scDevice.setDeviceName(deviceName);
				scDevice.setLastTime(lastTime);
				scDevice.setProductId(productId);
				scDevice.setRemarks(remarks);
				scDevice.setStatus(status2);
				pd.setScDevice(scDevice);
				list.add(pd);
			}
		}
		
		return new ScSearchResponse(ScSearchCode.SUCCESS,ScSearchMsg.SUCCESS
				,true,list,total,pageSize);
	}

}
