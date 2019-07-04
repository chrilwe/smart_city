package com.smart_city.common.ucenter.response.msg;

public interface ScUcenterMsg {
	public static final String SUCCESS = "请求成功";
	public static final String PARAM_NULL = "请求参数为空";
	public static final String SYSTEM_ERROR = "系统异常";
	public static final String ERROR_PARAMS = "非法的请求参数";
	public static final String PASSWORD_FORMAT_ERROR = "错误的密码格式";
	public static final String PHONENUM_ISUSED = "手机号码已经被注册";
	public static final String EMAIL_ISUSED = "邮箱已经被注册";
	public static final String CODE_ERROR = "验证码错误";
	public static final String AUTH_FAIL = "认证失败";
	public static final String AUTH_USERNAME_OR_PASSWORD_ERROR = "用户名或者密码错误";
}
