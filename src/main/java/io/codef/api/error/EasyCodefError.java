package io.codef.api.error;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum EasyCodefError {

    INVALID_JSON("CF-00002", "json형식이 올바르지 않습니다."),
    INVALID_PARAMETER("CF-00007", "요청 파라미터가 올바르지 않습니다."),
    UNSUPPORTED_ENCODING("CF-00009", "지원하지 않는 형식으로 인코딩된 문자열입니다."),

    EMPTY_CLIENT_INFO("CF-00014", "상품 요청을 위해서는 클라이언트 정보가 필요합니다. 클라이언트 아이디와 시크릿 정보를 설정하세요."),
    EMPTY_PUBLIC_KEY("CF-00015", "상품 요청을 위해서는 퍼블릭키가 필요합니다."),

    INVALID_2WAY_INFO("CF-03003", "2WAY 요청 처리를 위한 정보가 올바르지 않습니다. 응답으로 받은 항목을 그대로 2way요청 항목에 포함해야 합니다."),
    INVALID_2WAY_KEYWORD("CF-03004", "추가 인증(2Way)을 위한 요청은 requestCertification메서드를 사용해야 합니다."),

    BAD_REQUEST("CF-00400", "클라이언트 요청 오류로 인해 요청을 처리 할 수 ​​없습니다."),
    UNAUTHORIZED("CF-00401", "요청 권한이 없습니다."),
    FORBIDDEN("CF-00403", "잘못된 요청입니다."),
    NOT_FOUND("CF-00404", "요청하신 페이지(Resource)를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("CF-00405", "요청하신 방법(Method)이 잘못되었습니다."),

    LIBRARY_SENDER_ERROR("CF-09980", "통신 요청에 실패했습니다. 응답정보를 확인하시고 올바른 요청을 시도하세요."),
    SERVER_ERROR("CF-09999", "서버 처리중 에러가 발생 했습니다. 관리자에게 문의하세요.");

    private final String code;
    private final String message;

    EasyCodefError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private static final Map<Integer, EasyCodefError> HTTP_ERROR_MAP = createErrorMap();

    private static Map<Integer, EasyCodefError> createErrorMap() {
        Map<Integer, EasyCodefError> map = new HashMap<>();
        map.put(HttpURLConnection.HTTP_BAD_REQUEST, BAD_REQUEST);
        map.put(HttpURLConnection.HTTP_UNAUTHORIZED, UNAUTHORIZED);
        map.put(HttpURLConnection.HTTP_FORBIDDEN, FORBIDDEN);
        map.put(HttpURLConnection.HTTP_NOT_FOUND, NOT_FOUND);
        map.put(HttpURLConnection.HTTP_BAD_METHOD, METHOD_NOT_ALLOWED);
        return Collections.unmodifiableMap(map);
    }

    public static EasyCodefError fromHttpStatus(int statusCode) {
        EasyCodefError error = HTTP_ERROR_MAP.get(statusCode);
        return (error != null) ? error : SERVER_ERROR;
    }
}
