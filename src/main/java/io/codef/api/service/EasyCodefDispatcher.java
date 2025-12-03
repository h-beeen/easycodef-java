package io.codef.api.service;

import com.alibaba.fastjson2.JSON;

import io.codef.api.EasyCodefToken;
import io.codef.api.constant.CodefServiceType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;

public class EasyCodefDispatcher {

	private final EasyCodefToken token;
	private final CodefServiceType codefServiceType;
	private final EasyCodefApiService apiService;

	public EasyCodefDispatcher(EasyCodefToken token, CodefServiceType codefServiceType,
		EasyCodefApiService apiService) {
		this.token = token;
		this.codefServiceType = codefServiceType;
		this.apiService = apiService;
	}

	public EasyCodefResponse dispatchRequest(EasyCodefRequest request) {
		String urlPath = codefServiceType.getHost() + request.getProductUrl();
		String bearerToken = token.getValidAccessToken();
		String jsonBody = JSON.toJSONString(request.getParameterMap());
		Integer customTimeout = request.getCustomTimeout();

		return apiService.requestProduct(urlPath, bearerToken, jsonBody, customTimeout);
	}
}
