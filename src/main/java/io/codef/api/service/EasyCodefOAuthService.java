package io.codef.api.service;

import static io.codef.api.constant.CodefHost.*;
import static io.codef.api.constant.CodefPath.*;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpHeaders;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefOAuthService extends EasyCodefService {

	public EasyCodefOAuthService(ApacheHttpClient httpClient) {
		super(httpClient);
	}

	public EasyCodefResponse requestToken(String basicToken) {
		HttpPost request = HttpRequestBuilder.builder()
			.url(OAUTH_DOMAIN + GET_TOKEN)
			.header(HttpHeaders.AUTHORIZATION, basicToken)
			.build();

		return sendRequest(request);
	}
}
