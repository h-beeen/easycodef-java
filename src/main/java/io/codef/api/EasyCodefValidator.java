package io.codef.api;

import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefResponse;

import java.util.Map;

import static io.codef.api.ResponseHandler.handleErrorResponse;
import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.error.EasyCodefError.*;
import static io.codef.api.util.JsonUtil.*;

public class EasyCodefValidator {

    private EasyCodefValidator() {}

    protected static EasyCodefResponse validateRequest(EasyCodefProperties properties, CodefServiceType serviceType) {
        if (properties.checkClientInfo(serviceType)) {
            return handleErrorResponse(EMPTY_CLIENT_INFO);
        }

        if (properties.checkPublicKey()) {
            return handleErrorResponse(EMPTY_PUBLIC_KEY);
        }

        return null;
    }

    protected static boolean checkTwoWayInfo(Map<String, Object> parameterMap) {
        Object is2WayObj = parameterMap.get(IS_2WAY);
        if (!Boolean.TRUE.equals(is2WayObj)) {
            return false;
        }

        Object twoWayInfoObj = parameterMap.get(TWO_WAY_INFO);

        Map<String, Object> twoWayInfoMap = mapper().convertValue(twoWayInfoObj, mapTypeRef());

        return twoWayInfoMap.containsKey(JOB_INDEX)
                && twoWayInfoMap.containsKey(THREAD_INDEX)
                && twoWayInfoMap.containsKey(JTI)
                && twoWayInfoMap.containsKey(TWO_WAY_TIMESTAMP);
    }

    protected static boolean checkTwoWayKeyword(Map<String, Object> parameterMap) {
        return parameterMap == null || (!parameterMap.containsKey(IS_2WAY) && !parameterMap.containsKey(TWO_WAY_INFO));
    }
}
