package io.codef.api.service;

import static io.codef.api.constant.CodefConstant.*;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpHeaders;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefApiService extends EasyCodefService {

	public EasyCodefApiService(HttpClient httpClient) {
		super(httpClient);
	}

	public EasyCodefResponse requestProduct(String urlPath, String bearerToken, String jsonBody,
		Integer customTimeout) {
		HttpPost request = HttpRequestBuilder.builder()
			.url(urlPath)
			.header(HttpHeaders.AUTHORIZATION, bearerToken)
			.header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.getValue())
			.body(jsonBody)
			.timeout(customTimeout)
			.build();

		return sendRequest(request);
	}
}
