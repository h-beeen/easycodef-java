package io.codef.api.dto;

import java.util.Map;

public class EasyCodefRequest {

	private final String productUrl;
	private final Map<String, Object> parameterMap;
	private final Integer customTimeout;

	EasyCodefRequest(String productUrl, Map<String, Object> parameterMap, Integer customTimeout) {
		this.productUrl = productUrl;
		this.parameterMap = parameterMap;
		this.customTimeout = customTimeout;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	public Integer getCustomTimeout() {
		return customTimeout;
	}
}
