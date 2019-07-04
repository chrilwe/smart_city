package com.iot.smart_city.message.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iot.smart_city.model.message.ScMessage;

public interface ScMessageMapper {
	public int add(ScMessage scMessage);
	public int deleteById(String messageId);
	public int update(ScMessage scMessage);
	public ScMessage findById(String messageId);
	public int updateResendTimes(@Param("messageId")String messageId,
			@Param("resendTimes")int resendTimes,@Param("updateTime")Date updateTime,
			@Param("status")String status,@Param("version")int version);
	public int countByStatus(@Param("status")String status);
	public List<ScMessage> findList(@Param("start")int start,@Param("size")int size,
			@Param("status")String status);
}
