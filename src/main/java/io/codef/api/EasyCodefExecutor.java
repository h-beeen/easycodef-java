package io.codef.api;

import com.alibaba.fastjson2.JSON;
import io.codef.api.dto.EasyCodefResponse;

import java.util.Map;

public class EasyCodefExecutor {

    private final EasyCodefTokenManager tokenManager;

    public EasyCodefExecutor(EasyCodefTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    protected String execute(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) {
        String accessToken = tokenManager.getValidAccessToken();

        String urlPath = serviceType.getServiceType() + productUrl;

        EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);
        return JSON.toJSONString(response);
    }
}
