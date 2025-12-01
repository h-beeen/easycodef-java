package io.codef.api.core;

import static io.codef.api.constants.HttpConstant.*;

import org.apache.hc.client5.http.classic.methods.HttpPost;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpResponse;

public class EasyCodefApiSender {

	private EasyCodefApiSender() {
	}

	static EasyCodefResponse sendRequest(HttpPost request) {
		HttpResponse httpResponse = HttpClient.postJson(request);

		if (httpResponse.getStatusCode() == STATUS_CONNECTION_ERROR) {
			throw CodefException.from(CodefError.IO_ERROR);
		} else if (httpResponse.getStatusCode() == STATUS_TIMEOUT_ERROR) {
			throw CodefException.from(CodefError.TIMEOUT_ERROR);
		}

		return ResponseHandler.processResponse(httpResponse);
	}
}
