package io.codef.api;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.http.HttpRequestBuilder;

import java.io.IOException;

public class EasyCodefConnector {

    private static final HttpClient httpClient = new ApacheHttpClient();

    private EasyCodefConnector() {}

    protected static EasyCodefResponse execute(HttpRequestBuilder builder) {
        try {
            HttpResponse httpResponse = httpClient.postJson(
                    builder.getUrl(),
                    builder.getHeaders(),
                    builder.getBody()
            );
            return ResponseHandler.processResponse(httpResponse);

        } catch (IOException e) {
            return ResponseHandler.handleErrorResponse(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
    }
}
