package com.iot.smart_city.manage.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScIotTopicApi;
import com.iot.smart_city.manage.device.service.ScIotTopicService;
import com.smart_city.common.base.ScBaseController;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.product.topic.request.ScIotTopicAddRequest;
import com.smart_city.common.product.topic.request.ScIotTopicDelRequest;
import com.smart_city.common.product.topic.request.ScIotTopicQueryRequest;
import com.smart_city.common.product.topic.request.ScIotTopicSubscribeRequest;
import com.smart_city.common.product.topic.request.ScIotTopicUpdateRequest;
import com.smart_city.common.product.topic.response.ScCancelSubscribeTopicResponse;
import com.smart_city.common.product.topic.response.ScIotTopicAddResponse;
import com.smart_city.common.product.topic.response.ScIotTopicDelResponse;
import com.smart_city.common.product.topic.response.ScIotTopicQueryResponse;
import com.smart_city.common.product.topic.response.ScIotTopicUpdateResponse;
import com.smart_city.common.product.topic.response.code.ScIotTopicCode;
import com.smart_city.common.product.topic.response.msg.ScIotTopicMsg;
/**
 * 订阅与发布主题
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/manage/topic")
public class ScIotTopicController extends ScBaseController implements ScIotTopicApi {
	
	@Autowired
	private ScIotTopicService scIotTopicService;
	
	/**
	 * 主题列表
	 */
	@Override
	@GetMapping("/query")
	public ScIotTopicQueryResponse scIotTopicQuery(ScIotTopicQueryRequest scIotTopicQueryRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 自定义topic
	 */
	@Override
	@PostMapping("/add")
	public ScIotTopicAddResponse scIotTopicAdd(ScIotTopicAddRequest scIotTopicAddRequest) {
		ScIotTopicAddResponse scIotTopicAdd = null;
		try {
			scIotTopicAdd = scIotTopicService.scIotTopicAdd(scIotTopicAddRequest);
		} catch (Exception e) {
			return new ScIotTopicAddResponse(ScIotTopicCode.SYSTEM_ERROR,
					ScIotTopicMsg.SYSTEM_ERROR,false);
		}
		return scIotTopicAdd ;
	}
	
	/**
	 * 删除topic
	 */
	@Override
	public ScIotTopicDelResponse scIotTopicDel(ScIotTopicDelRequest scIotTopicDelRequest) {
		ScIotTopicDelResponse scIotTopicDel = null;
		try {
			scIotTopicDel = scIotTopicService.scIotTopicDel(scIotTopicDelRequest);
		} catch (Exception e) {
			return new ScIotTopicDelResponse(ScIotTopicCode.SYSTEM_ERROR,
					ScIotTopicMsg.SYSTEM_ERROR,false);
		}
		return scIotTopicDel;
	}
	
	/**
	 * 更新topic
	 */
	@Override
	public ScIotTopicUpdateResponse scIotTopicUpdate(ScIotTopicUpdateRequest scIotTopicUpdateRequest) {
		ScIotTopicUpdateResponse scIotTopicUpdate = null;
		try {
			scIotTopicUpdate = scIotTopicService.scIotTopicUpdate(scIotTopicUpdateRequest);
		} catch (Exception e) {
			return new ScIotTopicUpdateResponse(ScIotTopicCode.SYSTEM_ERROR,
					ScIotTopicMsg.SYSTEM_ERROR,false);
		}
		return scIotTopicUpdate;
	}
	
	/**
	 * 订阅主题消息
	 */
	@Override
	@GetMapping("/subscribe")
	public ScBaseResponse subscribe(ScIotTopicSubscribeRequest subscribeRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 取消订阅
	 */
	@Override
	public ScCancelSubscribeTopicResponse cancelSubscribe(String topicIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
