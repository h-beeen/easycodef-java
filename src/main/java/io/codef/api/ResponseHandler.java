package io.codef.api;

import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import java.util.Collections;
import java.util.Map;

public class ResponseHandler {

    public static EasyCodefResponse fromTokenResponse(Map<String, Object> tokenMap) {
        if (tokenMap == null || tokenMap.isEmpty()) {
            return fromError(EasyCodefError.LIBRARY_SENDER_ERROR, "Empty token response");
        }

        return new EasyCodefResponse(null, tokenMap);
    }

    @SuppressWarnings("unchecked")
    public static EasyCodefResponse fromRawMap(Map<String, Object> map) {
        if (map == null) {
            return new EasyCodefResponse(null, null);
        }

        Map<String, Object> resultMap = null;
        Object data = null;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (EasyCodefConstant.RESULT.equals(key)) {
                resultMap = (Map<String, Object>) value;
            } else if (EasyCodefConstant.DATA.equals(key)) {
                data = value;
            }
        }

        EasyCodefResponse.Result result = null;
        if (resultMap != null) {
            result = new EasyCodefResponse.Result(
                    (String) resultMap.get(EasyCodefConstant.CODE),
                    (String) resultMap.get(EasyCodefConstant.EXTRA_MESSAGE),
                    (String) resultMap.get(EasyCodefConstant.MESSAGE),
                    (String) resultMap.get(EasyCodefConstant.TRANSACTION_ID)
            );
        }

        return new EasyCodefResponse(result, data);
    }

    public static EasyCodefResponse fromError(EasyCodefError error) {
        return fromError(error, null);
    }

    public static EasyCodefResponse fromError(EasyCodefError error, String extraMessage) {
        if (error == null) {
            return new EasyCodefResponse(null, Collections.<String, Object>emptyMap());
        }

        String resolvedExtraMessage =
                (extraMessage != null && !extraMessage.isEmpty())
                        ? extraMessage
                        : "";

        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),
                resolvedExtraMessage,
                error.getMessage(),
                null
        );

        return new EasyCodefResponse(result, Collections.<String, Object>emptyMap());
    }
}
