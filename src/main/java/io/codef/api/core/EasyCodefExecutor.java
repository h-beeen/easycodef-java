package io.codef.api.core;

import java.util.Map;

import io.codef.api.auth.EasyCodefTokenManager;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefResponse;

public class EasyCodefExecutor {

    private final EasyCodefTokenManager tokenManager;

    public EasyCodefExecutor(EasyCodefTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public EasyCodefResponse execute(String productUrl, CodefServiceType serviceType, Map<String, Object> parameterMap, Integer customTimeout) {
        String accessToken = tokenManager.getValidAccessToken();
        String urlPath = serviceType.getHost() + productUrl;

        return EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap, customTimeout);
    }
}
