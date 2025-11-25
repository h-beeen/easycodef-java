package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.http.HttpRequestBuilder;

import java.util.Map;

import static io.codef.api.EasyCodefConnector.execute;
import static io.codef.api.util.JsonUtil.mapper;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    protected static EasyCodefResponse publishToken(String oauthToken) {
        HttpRequestBuilder httpRequestBuilder = HttpRequestBuilder.builder()
                .url(CodefHost.OAUTH_DOMAIN + CodefPath.GET_TOKEN)
                .header("Authorization", oauthToken);
        return execute(httpRequestBuilder);
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
            return execute(requestBuilder);
        } catch (JsonProcessingException e) {
            return ResponseHandler.handleErrorResponse(EasyCodefError.INVALID_JSON, e.getMessage());
        }
    }
}
