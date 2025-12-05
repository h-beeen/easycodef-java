package io.codef.api.service;

import static io.codef.api.constant.CodefHost.*;
import static io.codef.api.constant.CodefPath.*;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefOAuthService extends EasyCodefService {

	public EasyCodefOAuthService(CodefHttpClient httpClient) {
		super(httpClient);
	}

	public EasyCodefResponse requestToken(String basicToken) {
		CodefHttpRequest request = HttpRequestBuilder.builder()
			.url(OAUTH_DOMAIN + GET_TOKEN)
			.header("Authorization", basicToken)
			.build();

		return sendRequest(request);
	}
}
