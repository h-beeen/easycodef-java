package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpRequestBuilder;

import java.util.Map;

import static io.codef.api.constants.CodefHost.OAUTH_DOMAIN;
import static io.codef.api.constants.CodefPath.GET_TOKEN;
import static io.codef.api.error.EasyCodefError.INVALID_JSON;
import static io.codef.api.util.JsonUtil.mapper;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    protected static EasyCodefResponse publishToken(String oauthToken) {
        HttpRequestBuilder httpRequestBuilder = HttpRequestBuilder.builder()
                .url(OAUTH_DOMAIN + GET_TOKEN)
                .header("Authorization", oauthToken);
        return EasyCodefConnector.execute(httpRequestBuilder);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        try {
            String jsonBody = mapper().writeValueAsString(bodyMap);
            HttpRequestBuilder requestBuilder = HttpRequestBuilder.builder()
                    .url(urlPath)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .body(jsonBody);
            return EasyCodefConnector.execute(requestBuilder);
        } catch (JsonProcessingException e) {
            return ResponseHandler.handleErrorResponse(INVALID_JSON, e.getMessage());
        }
    }
}
