package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.http.HttpRequestBuilder;

import java.io.IOException;
import java.util.Map;

public class EasyCodefConnector {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient httpClient = new ApacheHttpClient();

    protected static EasyCodefResponse execute(HttpRequestBuilder builder) {
        try {
            HttpResponse httpResponse = httpClient.postJson(
                    builder.getUrl(),
                    builder.getHeaders(),
                    builder.getBody()
            );
            return ResponseHandler.processResponse(httpResponse, builder.getUrl());

        } catch (IOException e) {
            return ResponseHandler.fromError(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
    }

    protected static EasyCodefResponse publishToken(String oauthToken) {
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(CodefHost.OAUTH_DOMAIN + CodefPath.GET_TOKEN)
                .header("Authorization", oauthToken);
        return execute(httpRequestBuilder);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        try {
            String jsonBody = MAPPER.writeValueAsString(bodyMap);
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder(urlPath)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .body(jsonBody);
            return execute(requestBuilder);
        } catch (JsonProcessingException e) {
            return ResponseHandler.fromError(EasyCodefError.INVALID_JSON, e.getMessage());
        }
    }
}
