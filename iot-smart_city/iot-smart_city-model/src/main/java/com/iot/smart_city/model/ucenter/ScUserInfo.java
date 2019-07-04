package com.iot.smart_city.model.ucenter;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScUserInfo {
	private String id;
    private String username;
    private String name;
    private String utype;
    private String userpic;
    private String accessToken;
    private String jwtToken;
}
