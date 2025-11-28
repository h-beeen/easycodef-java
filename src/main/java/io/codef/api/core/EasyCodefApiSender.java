package io.codef.api.core;

import io.codef.api.handler.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpClientFactory;

public class EasyCodefApiSender {

    private EasyCodefApiSender() {}

    protected static EasyCodefResponse sendRequest(HttpUriRequest request) {
        HttpClient httpClient = HttpClientFactory.getInstance();

        HttpResponse httpResponse = httpClient.postJson(request);
        if (httpResponse.getStatusCode() == -1) {
            throw CodefException.from(CodefError.IO_ERROR);
        }

        return ResponseHandler.processResponse(httpResponse);
    }
}
