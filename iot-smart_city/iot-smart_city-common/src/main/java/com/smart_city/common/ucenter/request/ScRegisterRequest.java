package com.smart_city.common.ucenter.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScRegisterRequest extends ScBaseRequest {
	private String username;
    private String password;
    private String salt;
    private String name;
    private String utype;
    private String birthday;
    private String userpic;
    private String sex;
    private String email;
    private String phone;
    private String status;
}
