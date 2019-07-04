package com.iot.smart_city.util.mqtt_client;


/**
 * 邮箱格式校验
 * @author Administrator
 *
 */
public class EmailValidateUtil {
	
	public static boolean validate(String email) {
		String regex = "[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\\.[a-zA-Z]+)+";
		if(email.matches(regex)) {
			return true;
		}
		return false;
	}
	
	/*public static void main(String[] args) {
		boolean validate = EmailValidateUtil.validate("1@.com");
		if(validate) {
			System.out.println("邮箱正确");
		} else {
			System.out.println("邮箱错误");
		}
	}*/
}
