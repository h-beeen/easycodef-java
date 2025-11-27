package io.codef.api;

import com.alibaba.fastjson2.JSON;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpRequestBuilder;

import java.util.Map;

import static io.codef.api.constants.CodefHost.OAUTH_DOMAIN;
import static io.codef.api.constants.CodefPath.GET_TOKEN;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    protected static EasyCodefResponse publishToken(String oauthToken) {
        HttpRequestBuilder httpRequestBuilder = HttpRequestBuilder.builder()
                .url(OAUTH_DOMAIN + GET_TOKEN)
                .header("Authorization", oauthToken);

        return EasyCodefApiSender.execute(httpRequestBuilder);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        String jsonBody = JSON.toJSONString(bodyMap);
        HttpRequestBuilder requestBuilder = HttpRequestBuilder.builder()
                .url(urlPath)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(jsonBody);

        return EasyCodefApiSender.execute(requestBuilder);
    }
}
