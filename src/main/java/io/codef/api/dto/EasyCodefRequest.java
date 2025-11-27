package io.codef.api.dto;

import io.codef.api.EasyCodefServiceType;

import java.util.Map;

public class EasyCodefRequest {
    private final String productUrl;
    private final EasyCodefServiceType serviceType;
    private final Map<String, Object> parameterMap;

    protected EasyCodefRequest(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) {
        this.productUrl = productUrl;
        this.serviceType = serviceType;
        this.parameterMap = parameterMap;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public EasyCodefServiceType getServiceType() {
        return serviceType;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}
