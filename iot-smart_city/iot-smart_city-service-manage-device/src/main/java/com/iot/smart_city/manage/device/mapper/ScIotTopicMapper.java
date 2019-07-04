package com.iot.smart_city.manage.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iot.smart_city.model.topic.ScIotTopic;

public interface ScIotTopicMapper {
	public void addTopic(ScIotTopic topic);
	public void deleteTopic(String id);
	public void updateTopic(ScIotTopic topic);
	public ScIotTopic findById(String id);
	public List<ScIotTopic> queryList(@Param("start")int start,@Param("size")int size);
	public ScIotTopic findByIdAndProductId(@Param("topicId")String topicId,@Param("productId")String productId);
}
