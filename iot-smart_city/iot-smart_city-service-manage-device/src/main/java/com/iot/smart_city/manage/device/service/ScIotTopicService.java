package com.iot.smart_city.manage.device.service;

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

public interface ScIotTopicService {
	// topic 列表
	public ScIotTopicQueryResponse scIotTopicQuery(ScIotTopicQueryRequest scIotTopicQueryRequest);

	// 自定义topic
	public ScIotTopicAddResponse scIotTopicAdd(ScIotTopicAddRequest scIotTopicAddRequest);

	// 删除topic
	public ScIotTopicDelResponse scIotTopicDel(ScIotTopicDelRequest scIotTopicDelRequest);

	// 更新topic
	public ScIotTopicUpdateResponse scIotTopicUpdate(ScIotTopicUpdateRequest scIotTopicUpdateRequest);
	
	//订阅主题
	public ScBaseResponse subscribe(ScIotTopicSubscribeRequest subscribeRequest);
	
	//取消订阅
	public ScCancelSubscribeTopicResponse cancelSubscribe(String topicIds);
}
