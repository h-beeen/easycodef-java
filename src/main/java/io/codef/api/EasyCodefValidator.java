package io.codef.api;

import com.alibaba.fastjson2.JSON;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.error.CodefError.*;

public class EasyCodefValidator {

    private EasyCodefValidator() {}

    protected static void validateRequest(EasyCodefProperties properties) {
        if (properties.checkClientInfo()) {
            throw CodefException.from(EMPTY_CLIENT_INFO);
        }

        if (properties.checkPublicKey()) {
            throw CodefException.from(EMPTY_PUBLIC_KEY);
        }
    }

    protected static boolean checkTwoWayInfo(Map<String, Object> parameterMap) {
        Object is2WayObj = parameterMap.get(IS_2WAY);
        if (!Boolean.TRUE.equals(is2WayObj)) {
            return false;
        }

        Object twoWayInfoObj = parameterMap.get(TWO_WAY_INFO);
        String jsonString = JSON.toJSONString(twoWayInfoObj);

        Map<String, Object> twoWayInfoMap = JSON.parseObject(jsonString);

        return twoWayInfoMap.containsKey(JOB_INDEX)
                && twoWayInfoMap.containsKey(THREAD_INDEX)
                && twoWayInfoMap.containsKey(JTI)
                && twoWayInfoMap.containsKey(TWO_WAY_TIMESTAMP);
    }

    protected static boolean checkTwoWayKeyword(Map<String, Object> parameterMap) {
        return parameterMap == null || (!parameterMap.containsKey(IS_2WAY) && !parameterMap.containsKey(TWO_WAY_INFO));
    }

    protected static <T> T validateNotNullOrThrow(T object, CodefError codefError) {
        if (object == null) {
            throw CodefException.from(codefError);
        }

        return object;
    }
}
