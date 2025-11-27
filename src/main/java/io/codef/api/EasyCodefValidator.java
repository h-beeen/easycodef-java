package io.codef.api;

import com.alibaba.fastjson2.JSONObject;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;

public class EasyCodefValidator {

    private EasyCodefValidator() {}

    public static <T> T validateNotNullOrThrow(T object, CodefError codefError) {
        if (object == null) {
            throw CodefException.from(codefError);
        }

        return object;
    }

    public static String validatePathOrThrow(String productUrl, CodefError codefError) {
        if (!productUrl.startsWith(PATH_PREFIX)) {
            throw CodefException.from(codefError);
        }

        return productUrl;
    }

    public static void validateTwoWayKeywordsOrThrow(Map<String, Object> parameterMap) {
        if (parameterMap == null) {
            return;
        }

        if (parameterMap.containsKey(IS_2WAY) || parameterMap.containsKey(TWO_WAY_INFO)) {
            throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
        }
    }

    protected static void validateTwoWayInfoOrThrow(Map<String, Object> parameterMap) {
        Object is2WayObj = parameterMap.get(IS_2WAY);
        if (Boolean.FALSE.equals(is2WayObj)) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }

        Object twoWayInfoObj = parameterMap.get(TWO_WAY_INFO);

        Map<String, Object> twoWayInfoMap = JSONObject.from(twoWayInfoObj);

        boolean hasAllKeys = twoWayInfoMap.containsKey(JOB_INDEX)
                && twoWayInfoMap.containsKey(THREAD_INDEX)
                && twoWayInfoMap.containsKey(JTI)
                && twoWayInfoMap.containsKey(TWO_WAY_TIMESTAMP);

        if (!hasAllKeys) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }
    }
}
