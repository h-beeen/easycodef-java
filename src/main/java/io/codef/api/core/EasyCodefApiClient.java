package io.codef.api.core;

import static io.codef.api.constants.CodefHost.*;
import static io.codef.api.constants.CodefPath.*;

import java.util.Map;

import io.codef.api.util.AuthorizationUtil;
import org.apache.http.client.methods.HttpUriRequest;

import com.alibaba.fastjson2.JSON;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpRequestBuilder;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    public static EasyCodefResponse publishToken(String oauthToken) {
        String basicToken = AuthorizationUtil.createBasicAuth(oauthToken);
        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(OAUTH_DOMAIN + GET_TOKEN)
                .header("Authorization", basicToken)
                .build();

        return EasyCodefApiSender.sendRequest(request);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        String jsonBody = JSON.toJSONString(bodyMap);
        String bearerToken = AuthorizationUtil.createBearerAuth(accessToken);
        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(urlPath)
                .header("Authorization", bearerToken)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .build();

        return EasyCodefApiSender.sendRequest(request);
    }
}
