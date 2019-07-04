package com.smart_city.common.product.response.code;

public interface ScProductCode {
	public static final int SUCCESS = 20000;
	public static final int SYSTEM_ERROR = 20001;//系统异常
	public static final int PARAM_NULL =20002;//请求参数为空
	public static final int PARAM_NOT_ALLOW_NULL = 20003;//必填参数不能为空
	public static final int ERROR_PARAM = 20004;//非法请求参数
	public static final int PRODUCT_NOT_FOUND = 20005;//产品不存在
}
