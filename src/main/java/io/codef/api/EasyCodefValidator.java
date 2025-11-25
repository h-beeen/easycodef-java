package io.codef.api;

import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.constants.CodefConstant.IS_2WAY;
import static io.codef.api.constants.CodefConstant.JTI;
import static io.codef.api.constants.CodefConstant.THREAD_INDEX;
import static io.codef.api.constants.CodefConstant.TWO_WAY_INFO;
import static io.codef.api.constants.CodefConstant.TWO_WAY_TIMESTAMP;
import static io.codef.api.util.JsonUtil.mapTypeRef;
import static io.codef.api.util.JsonUtil.mapper;

public class EasyCodefValidator {

    private EasyCodefValidator() {}

    protected static EasyCodefResponse validateCommonRequirements(EasyCodefProperties properties, CodefServiceType serviceType) {
        if (properties.checkClientInfo(serviceType)) {
            return ResponseHandler.handleErrorResponse(EasyCodefError.EMPTY_CLIENT_INFO);
        }

        if (properties.checkPublicKey()) {
            return ResponseHandler.handleErrorResponse(EasyCodefError.EMPTY_PUBLIC_KEY);
        }

        return null;
    }

    protected static boolean checkTwoWayInfo(Map<String, Object> parameterMap) {
        Object is2WayObj = parameterMap.get(IS_2WAY);
        if (!(is2WayObj instanceof Boolean) || !((Boolean) is2WayObj)) {
            return false;
        }

        Object twoWayInfoObj = parameterMap.get(TWO_WAY_INFO);

        try {
            Map<String, Object> twoWayInfoMap = mapper().convertValue(twoWayInfoObj, mapTypeRef());

            return twoWayInfoMap.containsKey(JOB_INDEX)
                    && twoWayInfoMap.containsKey(THREAD_INDEX)
                    && twoWayInfoMap.containsKey(JTI)
                    && twoWayInfoMap.containsKey(TWO_WAY_TIMESTAMP);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    protected static boolean checkTwoWayKeyword(Map<String, Object> parameterMap) {
        return parameterMap == null || (!parameterMap.containsKey(IS_2WAY) && !parameterMap.containsKey(TWO_WAY_INFO));
    }
}
