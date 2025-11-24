package io.codef.api;

import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import java.util.Collections;
import java.util.Map;

public class ResponseHandler {

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
        return fromError(error, "");
    }

    public static EasyCodefResponse fromError(EasyCodefError error, String extraMessage) {
        if (error == null) {
            return new EasyCodefResponse(null, Collections.<String, Object>emptyMap());
        }

        String resolvedExtraMessage =
                (extraMessage != null && !extraMessage.isEmpty())
                        ? extraMessage
                        : error.getExtraMessage(); // 현재는 null이지만, 구조 유지

        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),         // "CF-00014"
                resolvedExtraMessage,    // ""  (fromError(error) 호출시)
                error.getMessage(),      // "상품 요청을 위해서는 클라이언트 정보가 필요합니다...."
                null
        );

        return new EasyCodefResponse(result, Collections.<String, Object>emptyMap());
    }
}
