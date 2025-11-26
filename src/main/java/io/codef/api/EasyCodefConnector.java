package io.codef.api;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpClientFactory;
import io.codef.api.http.HttpRequestBuilder;

import static io.codef.api.ResponseHandler.handleErrorResponse;
import static io.codef.api.ResponseHandler.processResponse;
import static io.codef.api.error.EasyCodefError.LIBRARY_SENDER_ERROR;

public class EasyCodefConnector {

    private EasyCodefConnector() {}

    protected static EasyCodefResponse execute(HttpRequestBuilder builder) {
        HttpClient httpClient = HttpClientFactory.getInstance();
        HttpResponse httpResponse = httpClient.postJson(
                builder.getUrl(),
                builder.getHeaders(),
                builder.getBody()
        );

        if (httpResponse.getStatusCode() == -1) {
            return handleErrorResponse(LIBRARY_SENDER_ERROR, httpResponse.getBody());
        }

        return processResponse(httpResponse);
    }
}
