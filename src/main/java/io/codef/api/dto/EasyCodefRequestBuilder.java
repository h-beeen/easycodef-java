package io.codef.api.dto;

import io.codef.api.EasyCodefServiceType;

import java.util.HashMap;
import java.util.Map;

public class EasyCodefRequestBuilder {

    private String productUrl;
    private Map<String, Object> parameterMap = new HashMap<>();

    public static EasyCodefRequestBuilder builder() {
        return new EasyCodefRequestBuilder();
    }

    public EasyCodefRequestBuilder productUrl(String productUrl) {
        this.productUrl = productUrl;
        return this;
    }

    public EasyCodefRequestBuilder parameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
        return this;
    }

    public EasyCodefRequest build() {
        return new EasyCodefRequest(productUrl, parameterMap);
    }
}
