package com.iot.smart_city.server.websocket.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import com.alibaba.fastjson.JSON;


public class JwtTokenUtil {
	
	//解析令牌
	public static Map paraseJwtToken(String jwtToken) {
		Map map = null;
		try {
			Jwt decode = JwtHelper.decode(jwtToken);
			String claims = decode.getClaims();
			map = JSON.parseObject(claims, Map.class);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	//校验并解析令牌
	public static Map decodeAndVerify(String jwt) {
		SignatureVerifier verifier = new RsaVerifier(getPublicKey());
		Jwt decodeAndVerify = JwtHelper.decodeAndVerify(jwt, verifier);
		String claims = decodeAndVerify.getClaims();
		if(StringUtils.isEmpty(claims)) {
			return null;
		}
		Map map = JSON.parseObject(claims, Map.class);
		return map;
	}
	
	
	//获取公钥
	private static String getPublicKey() {
		Resource resource = new ClassPathResource("publickey.txt");
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			return br.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			return null;
		}
	}
	
	//创建令牌
	public static String createJwtToken(String rsaFileName, String secret, String alias, String password,String fromHost, String targetHost) {
		// 获取证书
		Resource resource = new ClassPathResource(rsaFileName);
		// 秘钥工厂
		KeyStoreKeyFactory keyStoreFactory = new KeyStoreKeyFactory(resource, secret.toCharArray());
		// 秘钥对
		KeyPair keyPair = keyStoreFactory.getKeyPair(alias, password.toCharArray());
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 定义playload
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromHost", fromHost);
		map.put("targetHost", targetHost );
		// 生成令牌
		Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(privateKey));
		String jwtToken = jwt.getEncoded();
		return jwtToken;
	}
}
