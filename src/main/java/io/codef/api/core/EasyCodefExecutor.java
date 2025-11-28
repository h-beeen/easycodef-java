package io.codef.api.core;

import com.alibaba.fastjson2.JSON;

import io.codef.api.auth.EasyCodefTokenManager;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefResponse;

import java.util.Map;

public class EasyCodefExecutor {

    private final EasyCodefTokenManager tokenManager;

    public EasyCodefExecutor(EasyCodefTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public String execute(String productUrl, CodefServiceType serviceType, Map<String, Object> parameterMap) {
        String accessToken = tokenManager.getValidAccessToken();
        String urlPath = serviceType.getHost() + productUrl;
        EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);

        return JSON.toJSONString(response);
    }
}
