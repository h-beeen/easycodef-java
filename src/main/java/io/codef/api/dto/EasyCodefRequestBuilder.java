package io.codef.api.dto;

import java.util.HashMap;
import java.util.Map;

import io.codef.api.handler.CodefValidator;
import io.codef.api.error.CodefError;

public class EasyCodefRequestBuilder {

    private String productUrl;
    private Map<String, Object> parameterMap = new HashMap<>();
    private Integer customTimeout;

    public static EasyCodefRequestBuilder builder() {
        return new EasyCodefRequestBuilder();
    }

    public EasyCodefRequestBuilder productUrl(String productUrl) {
        this.productUrl = CodefValidator.validatePathOrThrow(productUrl, CodefError.INVALID_PATH_REQUESTED);
        return this;
    }

    public EasyCodefRequestBuilder parameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = CodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
        return this;
    }

    public EasyCodefRequestBuilder customTimeout(Integer customTimeout) {
        this.customTimeout = customTimeout;
        return this;
    }

    public EasyCodefRequest build() {
        validateProperties();

        return new EasyCodefRequest(productUrl, parameterMap, customTimeout);
    }

    private void validateProperties() {
        CodefValidator.validateNotNullOrThrow(productUrl, CodefError.EMPTY_PATH);
        CodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
    }
}
