package io.codef.api;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.util.JsonUtil;

import java.util.Map;

public class EasyCodefExecutor {

    private final EasyCodefTokenManager tokenManager;

    public EasyCodefExecutor(EasyCodefTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    protected String execute(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) {
        String accessToken = tokenManager.getAccessToken(serviceType);

        String urlPath = serviceType.getServiceType() + productUrl;

        EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);
        return JsonUtil.writeValueAsString(response);
    }
}
