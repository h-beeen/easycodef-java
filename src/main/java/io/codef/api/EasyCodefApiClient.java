package io.codef.api;

import com.alibaba.fastjson2.JSON;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpRequestBuilder;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

import static io.codef.api.constants.CodefHost.OAUTH_DOMAIN;
import static io.codef.api.constants.CodefPath.GET_TOKEN;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    protected static EasyCodefResponse publishToken(String oauthToken) {
        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(OAUTH_DOMAIN + GET_TOKEN)
                .header("Authorization", oauthToken)
                .build();

        return EasyCodefApiSender.execute(request);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        String jsonBody = JSON.toJSONString(bodyMap);
        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(urlPath)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .build();

        return EasyCodefApiSender.execute(request);
    }
}
