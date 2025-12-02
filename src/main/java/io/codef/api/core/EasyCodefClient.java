package io.codef.api.core;

import static io.codef.api.constants.CodefHost.*;
import static io.codef.api.constants.CodefPath.*;
import static io.codef.api.constants.HttpConstant.*;

import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpHeaders;

import com.alibaba.fastjson2.JSON;

import io.codef.api.auth.EasyCodefToken;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpRequestBuilder;
import io.codef.api.http.HttpResponse;
import io.codef.api.util.AuthorizationUtil;

public class EasyCodefClient {

	private EasyCodefClient() {
	}

	public static EasyCodefResponse publishToken(String oauthToken) {
		String basicToken = AuthorizationUtil.createBasicAuth(oauthToken);

		HttpPost request = HttpRequestBuilder.builder()
			.url(OAUTH_DOMAIN + GET_TOKEN)
			.header(HttpHeaders.AUTHORIZATION, basicToken)
			.build();

		return sendRequest(request);
	}

	static EasyCodefResponse requestProduct(
		String urlPath,
		EasyCodefToken token,
		Map<String, Object> bodyMap,
		Integer customTimeout
	) {
		String jsonBody = JSON.toJSONString(bodyMap);
		String bearerToken = AuthorizationUtil.createBearerAuth(token.getAccessToken());

		HttpPost request = HttpRequestBuilder.builder()
			.url(urlPath)
			.header(HttpHeaders.AUTHORIZATION, bearerToken)
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(jsonBody)
			.timeout(customTimeout)
			.build();

		return sendRequest(request);
	}

	private static EasyCodefResponse sendRequest(HttpPost request) {
		HttpResponse httpResponse = HttpClient.postJson(request);

		if (httpResponse.getStatusCode() == STATUS_CONNECTION_ERROR) {
			throw CodefException.from(CodefError.IO_ERROR);
		} else if (httpResponse.getStatusCode() == STATUS_TIMEOUT_ERROR) {
			throw CodefException.from(CodefError.TIMEOUT_ERROR);
		}

		return ResponseHandler.processResponse(httpResponse);
	}
}
