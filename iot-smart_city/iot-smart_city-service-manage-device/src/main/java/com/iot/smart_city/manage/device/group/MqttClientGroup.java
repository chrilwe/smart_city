package com.iot.smart_city.manage.device.group;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * 存放mqtt客户端连接服务器的连接实例
 * @author chrilwe
 *
 */
public class MqttClientGroup {
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static MqttClientGroup group = null;
		static {
			group = new MqttClientGroup();
		}
		private static MqttClientGroup getInstance() {
			return group;
		}
	}
	
	/**
	 * 获取单例实例
	 */
	public static MqttClientGroup getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 初始化一个Map集合
	 */
	Map<String, MqttClient> map = null;
	private MqttClientGroup() {
		map = new HashMap<>();
	}
	
	/**
	 * 向集合中添加客户端连接实例
	 */
	public void put(String key, MqttClient mqttClient) {
		map.put(key, mqttClient);
	}
	
	/**
	 * 从集合 中移除连接实例
	 */
	public void remove(String key) {
		map.remove(key);
	}
	
	/**
	 * 从集合中取出一个连接实例
	 */
	public MqttClient get(String key) {
		return map.get(key);
	}
}
