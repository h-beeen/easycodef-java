package io.codef.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResponseHandler {

    private ResponseHandler() {
        // utility class
    }

    /**
     * CODEF에서 내려오는 원본 Map 응답을 DTO(EasyCodefResponse)로 변환한다.
     *
     * 기존 EasyCodefResponse(Map<String, Object> map) 생성자 역할을 대체.
     */
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
                // 결과 코드 정보
                resultMap = (Map<String, Object>) value;
            } else if (EasyCodefConstant.DATA.equals(key)) {
                // 결과 데이터 정보
                // CODEF 응답은 Map 또는 List<Map<String,Object>> 둘 다 가능
                if (value instanceof Map || value instanceof List) {
                    data = value;
                } else {
                    data = value; // 혹시 다른 타입이어도 그대로 보존
                }
            } else {
                // 예전 구현에서는 this.put(key, map.get(key)) 로 사용자 정의 파라미터를 응답에 실었음.
                // 현재 DTO 구조상 result / data 외의 값은 별도로 들고 있지 않으므로 여기서는 무시.
                // 만약 필요한 경우, DTO에 "extra" 필드를 추가해서 확장 가능.
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

    /**
     * HTTP 에러 코드 등으로부터 EasyCodefError가 결정된 상황에서
     * 에러 응답 DTO를 생성한다.
     *
     * 기존 EasyCodefResponse(EasyCodefError message) 생성자 역할 대체.
     */
    public static EasyCodefResponse fromError(EasyCodefError error) {
        return fromError(error, "");
    }

    /**
     * HTTP 에러 코드 등으로부터 EasyCodefError + 추가 메시지가 있는 경우
     * 에러 응답 DTO를 생성한다.
     *
     * 기존 EasyCodefResponse(EasyCodefError message, String extraMessage) 생성자 역할 대체.
     */
    public static EasyCodefResponse fromError(EasyCodefError error, String extraMessage) {
        if (error == null) {
            // 방어 코드: null이면 서버 에러로 간주
            return new EasyCodefResponse(null, Collections.<String, Object>emptyMap());
        }

        // extraMessage가 직접 들어온 값이 있으면 우선, 없으면 enum에 정의된 기본 extraMessage 사용
        String resolvedExtraMessage =
                (extraMessage != null && !extraMessage.isEmpty())
                        ? extraMessage
                        : error.getExtraMessage();

        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),
                resolvedExtraMessage,
                error.getMessage(),
                null // HTTP 레벨 에러 생성 시에는 transactionId 없음
        );

        // 예전 구현: data = new HashMap<>() 로 항상 빈 Map
        return new EasyCodefResponse(result, Collections.<String, Object>emptyMap());
    }

    public static Map<String, Object> toMap(EasyCodefResponse response) {
        Map<String, Object> map = new HashMap<>();

        if (response == null) {
            return map;
        }

        // result
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

    /**
     * 응답이 성공(CF-00000)인지 여부 판단.
     * (기존 코드에서 자주 쓰는 패턴을 헬퍼로 제공)
     */
    public static boolean isSuccess(EasyCodefResponse response) {
        return response != null &&
                "CF-00000".equals(response.code());
    }
}
