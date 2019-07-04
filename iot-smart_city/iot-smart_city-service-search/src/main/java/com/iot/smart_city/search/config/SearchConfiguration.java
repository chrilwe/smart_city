package com.iot.smart_city.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfiguration {
	
	@Value("${elasticsearch.host}")
	private String esHost;
	@Value("${elasticsearch.port}")
	private int esPort;
	
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		// 创建RestHighLevelClient客户端
		return new RestHighLevelClient(RestClient.builder(new HttpHost(esHost, esPort)));
	}
}
