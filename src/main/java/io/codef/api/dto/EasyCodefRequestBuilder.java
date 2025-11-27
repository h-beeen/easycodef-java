package io.codef.api.dto;

import io.codef.api.EasyCodefValidator;
import io.codef.api.error.CodefError;

import java.util.HashMap;
import java.util.Map;

public class EasyCodefRequestBuilder {

    private String productUrl;
    private Map<String, Object> parameterMap = new HashMap<>();

    public static EasyCodefRequestBuilder builder() {
        return new EasyCodefRequestBuilder();
    }

    public EasyCodefRequestBuilder productUrl(String productUrl) {
        this.productUrl = EasyCodefValidator.validatePathOrThrow(productUrl, CodefError.INVALID_PATH_REQUESTED);
        return this;
    }

    public EasyCodefRequestBuilder parameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = EasyCodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
        return this;
    }

    public EasyCodefRequest build() {
        validateProperties();

        return new EasyCodefRequest(productUrl, parameterMap);
    }

    private void validateProperties() {
        EasyCodefValidator.validateNotNullOrThrow(productUrl, CodefError.EMPTY_PATH);
        EasyCodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
    }
}
