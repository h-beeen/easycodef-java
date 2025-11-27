package io.codef.api.dto;

import io.codef.api.EasyCodefServiceType;

import java.util.Map;

public class EasyCodefRequest {
    private final String productUrl;
    private final Map<String, Object> parameterMap;

    protected EasyCodefRequest(String productUrl, Map<String, Object> parameterMap) {
        this.productUrl = productUrl;
        this.parameterMap = parameterMap;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}
