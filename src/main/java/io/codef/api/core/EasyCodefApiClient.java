package io.codef.api.core;

import static io.codef.api.constants.CodefHost.*;
import static io.codef.api.constants.CodefPath.*;

import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;

import com.alibaba.fastjson2.JSON;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpRequestBuilder;
import io.codef.api.util.AuthorizationUtil;

public class EasyCodefApiClient {

    private EasyCodefApiClient() {}

    public static EasyCodefResponse publishToken(String oauthToken) {
        String basicToken = AuthorizationUtil.createBasicAuth(oauthToken);

        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(OAUTH_DOMAIN + GET_TOKEN)
                .header(HttpHeaders.AUTHORIZATION, basicToken)
                .build();

        return EasyCodefApiSender.sendRequest(request);
    }

    static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap,
            Integer customTimeout
    ) {
        String jsonBody = JSON.toJSONString(bodyMap);
        String bearerToken = AuthorizationUtil.createBearerAuth(accessToken);

        HttpUriRequest request = HttpRequestBuilder.builder()
                .url(urlPath)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(jsonBody)
                .timeout(customTimeout)
                .build();

        return EasyCodefApiSender.sendRequest(request);
    }
}
