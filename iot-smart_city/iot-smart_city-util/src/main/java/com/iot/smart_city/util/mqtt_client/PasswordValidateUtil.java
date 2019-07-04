package com.iot.smart_city.util.mqtt_client;
/**
 * 密码格式校验
 * @author chrilwe
 *
 */
public class PasswordValidateUtil {
	
	//符合六位数即可
		public static boolean validate(String password) {
			int length = password.length();
			if(length != 6) {
				return false;
			}
			return true;
		}
}
