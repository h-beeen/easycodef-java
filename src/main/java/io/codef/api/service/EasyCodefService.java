package io.codef.api.service;

import org.apache.hc.client5.http.classic.methods.HttpPost;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.ApacheHttpClient;

public abstract class EasyCodefService {

	private final ApacheHttpClient httpClient;

	EasyCodefService(ApacheHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	EasyCodefResponse sendRequest(HttpPost request) {
		String httpResponse = httpClient.execute(request);

		return ResponseHandler.processResponse(httpResponse);
	}
}
