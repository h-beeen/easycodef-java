package io.codef.api.core;

import static io.codef.api.constants.HttpConstant.STATUS_CONNECTION_ERROR;
import static io.codef.api.constants.HttpConstant.STATUS_TIMEOUT_ERROR;

import org.apache.http.client.methods.HttpUriRequest;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpClientFactory;
import io.codef.api.handler.ResponseHandler;

public class EasyCodefApiSender {

    private EasyCodefApiSender() {}

    static EasyCodefResponse sendRequest(HttpUriRequest request) {
        HttpClient httpClient = HttpClientFactory.getInstance();
        HttpResponse httpResponse = httpClient.postJson(request);

        if (httpResponse.getStatusCode() == STATUS_CONNECTION_ERROR) {
            throw CodefException.from(CodefError.IO_ERROR);
        } else if (httpResponse.getStatusCode() == STATUS_TIMEOUT_ERROR) {
            throw CodefException.from(CodefError.TIMEOUT_ERROR);
        }

        return ResponseHandler.processResponse(httpResponse);
    }
}
