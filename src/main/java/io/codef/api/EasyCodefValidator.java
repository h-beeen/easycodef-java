package io.codef.api;

import com.alibaba.fastjson2.JSON;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;

public class EasyCodefValidator {

    private EasyCodefValidator() {}

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

    public static void validateTwoWayKeywordsOrThrow(Map<String, Object> parameterMap) {
        if (parameterMap == null) {
            return;
        }

        if (parameterMap.containsKey(IS_2WAY) || parameterMap.containsKey(TWO_WAY_INFO)) {
            throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
        }
    }

    public static <T> T validateNotNullOrThrow(T object, CodefError codefError) {
        if (object == null) {
            throw CodefException.from(codefError);
        }

        return object;
    }
}
