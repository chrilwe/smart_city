package com.smart_city.common.message.response.code;

public interface ScMessageCode {
	public static final int SUCCESS = 200;
	public static final int PARAM_NULL = 201;
	public static final int MESSAGE_EXIST = 202;
	public static final int MESSAGE_NO_EXIST = 203;
	public static final int MESSAGE_STATUS_ISNOT_SENDING = 204;
	public static final int MESSAGE_SENDTIMES_LIMIT = 205;
	public static final int SYSTEM_ERROR = 206;
	public static final int PARAM_ISNOTALLOW_NULL = 207;
}
