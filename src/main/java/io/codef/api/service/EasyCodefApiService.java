package io.codef.api.service;

import static io.codef.api.constant.CodefConstant.*;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefApiService extends EasyCodefService {

	public EasyCodefApiService(CodefHttpClient httpClient) {
		super(httpClient);
	}

	public EasyCodefResponse requestProduct(String urlPath, String bearerToken, String jsonBody,
		Integer customTimeout) {
		CodefHttpRequest request = HttpRequestBuilder.builder()
			.url(urlPath)
			.header("Authorization", bearerToken)
			.header("Content-Type", APPLICATION_JSON.getValue())
			.body(jsonBody)
			.timeout(customTimeout)
			.build();

		return sendRequest(request);
	}
}
