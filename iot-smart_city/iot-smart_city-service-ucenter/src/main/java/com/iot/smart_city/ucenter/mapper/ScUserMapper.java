package com.iot.smart_city.ucenter.mapper;

import com.iot.smart_city.model.ucenter.ScUser;

public interface ScUserMapper {
	public int add(ScUser scUser);
	public int deleteById(String userId);
	public int update(ScUser scUser);
	public ScUser findByUsername(String username);
	public ScUser findByPhone(String phone);
	public ScUser findByEmail(String email);
}
