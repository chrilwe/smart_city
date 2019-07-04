package Test;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;

public class Test {
	public static void main(String[] args) {
		String json = "{" + "\"accessToken\"" + ":" +"\"access\"";
		WebSocketMessage parseObject = JSON.parseObject(json, WebSocketMessage.class);
	}
}
