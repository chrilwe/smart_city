package com.iot.smart_city.util.mqtt_client;
/**
 * 手机号码校验
 * @author chrilwe
 *
 */
public class PhoneValidateUtil {
	
	public static boolean validate(String phone) {
		//中国移动
		String yingdong = "^((13[4-9])|(14[7-8])|(15[0-2,7-9])|(165)|(178)|(18[2-4,7-8])|(198))\\d{8}|(170[3,5,6])\\d{7}$";
		//中国联通
		String liantong = "^((13[0-2])|(14[5,6])|(15[5-6])|(16[6-7])|(17[1,5,6])|(18[5,6]))\\d{8}|(170[4,7-9])\\d{7}$";
		//中国电信
		String dianxing = "^((133)|(149)|(153)|(162)|(17[3,7])|(18[0,1,9])|(19[1,9]))\\d{8}|((170[0-2])|(174[0-5]))\\d{7}$";
		
		if(phone.matches(yingdong)) {
			return true;
		} else {
			boolean matches = phone.matches(liantong);
			if(matches) {
				return true;
			} else {
				boolean matches2 = phone.matches(dianxing);
				if(matches2) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*public static void main(String[] args) {
		boolean validate = PhoneValidateUtil.validate("1831602187");
		if(validate) {
			System.out.println("手机号码正确");
		} else {
			System.out.println("手机号码错误");
		}
	}*/
}
