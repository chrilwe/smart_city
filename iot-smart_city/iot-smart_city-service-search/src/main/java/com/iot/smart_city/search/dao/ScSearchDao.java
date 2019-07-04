package com.iot.smart_city.search.dao;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;

import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.response.ScSearchResponse;

public interface ScSearchDao {
	public ScBaseResponse addOrUpdate(IndexRequest indexRequest) throws IOException;
	public ScBaseResponse delete(DeleteRequest deleteRequest) throws IOException;
	public ScSearchResponse query(SearchRequest searchRequest, String searchType, long pageSize) throws IOException;
}
