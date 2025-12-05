package io.codef.api.service;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;

public abstract class EasyCodefService {

	private final CodefHttpClient httpClient;

	EasyCodefService(CodefHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	EasyCodefResponse sendRequest(CodefHttpRequest request) {
		String httpResponse = httpClient.execute(request);

		return ResponseHandler.processResponse(httpResponse);
	}
}
