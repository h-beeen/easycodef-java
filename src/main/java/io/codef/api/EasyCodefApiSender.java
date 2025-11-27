package io.codef.api;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.CodefException;
import io.codef.api.http.HttpClient;
import io.codef.api.http.HttpClientFactory;
import io.codef.api.http.HttpRequestBuilder;

import static io.codef.api.error.CodefError.IO_ERROR;

public class EasyCodefApiSender {

    private EasyCodefApiSender() {}

    protected static EasyCodefResponse execute(HttpRequestBuilder builder) {
        HttpClient httpClient = HttpClientFactory.getInstance();
        HttpResponse httpResponse = httpClient.postJson(
                builder.getUrl(),
                builder.getHeaders(),
                builder.getBody()
        );

        if (httpResponse.getStatusCode() == -1) {
            throw CodefException.from(IO_ERROR);
        }

        return ResponseHandler.processResponse(httpResponse);
    }
}
