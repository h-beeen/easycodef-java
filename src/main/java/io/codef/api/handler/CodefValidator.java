package io.codef.api.handler;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.constants.CodefConstant.TwoWay.*;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

public class CodefValidator {

    private CodefValidator() {}

    public static <T> T validateNotNullOrThrow(T object, CodefError codefError) {
        if (object == null) {
            throw CodefException.from(codefError);
        }

        return object;
    }

    public static String validatePathOrThrow(String productUrl, CodefError codefError) {
        if (productUrl == null || !productUrl.startsWith(PATH_PREFIX)) {
            throw CodefException.from(codefError);
        }

        return productUrl;
    }

    public static void validateTwoWayKeywordsOrThrow(Map<String, Object> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            return;
        }

        if (parameterMap.containsKey(IS_2WAY) || parameterMap.containsKey(INFO_KEY)) {
            throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
        }
    }

    public static void validateTwoWayInfoOrThrow(Map<String, Object> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            throw CodefException.from(CodefError.EMPTY_PARAMETER);
        }

        Object is2WayObj = parameterMap.get(IS_2WAY);
        if (Boolean.FALSE.equals(is2WayObj)) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }

        Object twoWayInfoObj = parameterMap.get(INFO_KEY);
        Map<String, Object> twoWayInfoMap = JSONObject.from(twoWayInfoObj);

        if (!twoWayInfoMap.keySet().containsAll(REQUIRED_KEYS)) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }
    }
}
