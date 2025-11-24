package io.codef.api;

import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResponseHandler {

    private ResponseHandler() { }

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
                if (value instanceof Map || value instanceof List) {
                    data = value;
                } else {
                    data = value;
                }
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
        return fromError(error, "");
    }

    public static EasyCodefResponse fromError(EasyCodefError error, String extraMessage) {
        if (error == null) {
            return new EasyCodefResponse(null, Collections.<String, Object>emptyMap());
        }

        String resolvedExtraMessage =
                (extraMessage != null && !extraMessage.isEmpty())
                        ? extraMessage
                        : error.getExtraMessage();

        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),
                resolvedExtraMessage,
                error.getMessage(),
                null
        );

        return new EasyCodefResponse(result, Collections.<String, Object>emptyMap());
    }

    public static Map<String, Object> toMap(EasyCodefResponse response) {
        Map<String, Object> map = new HashMap<>();

        if (response == null) {
            return map;
        }

        Map<String, Object> resultMap = new HashMap<>();
        if (response.getResult() != null) {
            resultMap.put(EasyCodefResponse.RESULT, response.getResult().getCode());
            resultMap.put(EasyCodefConstant.MESSAGE, response.getResult().getMessage());
            resultMap.put(EasyCodefConstant.EXTRA_MESSAGE, response.getResult().getExtraMessage());
            resultMap.put(EasyCodefConstant.TRANSACTION_ID, response.getResult().getTransactionId());
        }

        map.put(EasyCodefResponse.RESULT, resultMap);
        map.put(EasyCodefResponse.DATA, response.getData());

        return map;
    }

    public static boolean isSuccess(EasyCodefResponse response) {
        return response != null &&
                "CF-00000".equals(response.code());
    }
}
