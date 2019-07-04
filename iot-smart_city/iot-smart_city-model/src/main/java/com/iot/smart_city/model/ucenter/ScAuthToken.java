package com.iot.smart_city.model.ucenter;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScAuthToken {
	private String accessToken;
	private String jwtToken;
	private String refreshToken;
}
