package io.codef.api.core;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.constants.CodefHost.*;
import static io.codef.api.constants.CodefPath.*;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpHeaders;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefClient {

	private EasyCodefClient() {
	}

	public static EasyCodefResponse publishToken(String basicToken) {
		HttpPost request = HttpRequestBuilder.builder()
			.url(OAUTH_DOMAIN + GET_TOKEN)
			.header(HttpHeaders.AUTHORIZATION, basicToken)
			.build();

		return sendRequest(request);
	}

	static EasyCodefResponse requestProduct(
		String urlPath,
		String bearerToken,
		String jsonBody,
		Integer customTimeout
	) {
		HttpPost request = HttpRequestBuilder.builder()
			.url(urlPath)
			.header(HttpHeaders.AUTHORIZATION, bearerToken)
			.header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
			.body(jsonBody)
			.timeout(customTimeout)
			.build();

		return sendRequest(request);
	}

	private static EasyCodefResponse sendRequest(HttpPost request) {
		String httpResponse = HttpClient.postJson(request);

		return ResponseHandler.processResponse(httpResponse);
	}
}
