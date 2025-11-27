package io.codef.api.error;

public enum CodefError {

    INVALID_JSON("json형식이 올바르지 않습니다."),
    INVALID_PARAMETER("요청 파라미터가 올바르지 않습니다."),
    UNSUPPORTED_ENCODING("지원하지 않는 형식으로 인코딩된 문자열입니다."),

    EMPTY_CLIENT_INFO("상품 요청을 위해서는 클라이언트 정보가 필요합니다. 클라이언트 아이디와 시크릿 정보를 설정하세요."),

    PARSE_ERROR("클라이언트가 서버 응답을 기대한 형식대로 파싱하지 못했습니다. 요청 형식을 확인해 주세요."),

    RSA_ENCRYPTION_ERROR("RSA 암호화 과정에서 오류가 발생했습니다. PublicKey 값을 확인해 주세요."),

    INVALID_2WAY_INFO("2WAY 요청 처리를 위한 정보가 올바르지 않습니다. 응답으로 받은 항목을 그대로 2way요청 항목에 포함해야 합니다."),
    INVALID_2WAY_KEYWORD("추가 인증(2Way)을 위한 요청은 requestCertification메서드를 사용해야 합니다."),

    BAD_REQUEST("클라이언트 요청 오류로 인해 요청을 처리 할 수 ​​없습니다."),
    UNAUTHORIZED("요청 권한이 없습니다."),
    FORBIDDEN("잘못된 요청입니다."),
    NOT_FOUND("요청하신 페이지(Resource)를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("요청하신 방법(Method)이 잘못되었습니다."),

    EMPTY_SERVICE_TYPE("A membership version is required for the product request. Please set the desired membership version."),
    EMPTY_CLIENT_ID("Client ID is required for the product request. Please set the Client ID."),
    EMPTY_CLIENT_SECRET("Client Secret is required for the product request. Please set the Client Secret."),
    EMPTY_PUBLIC_KEY("A public key is required for the product request. Please set the public key information."),

    IO_ERROR("통신 요청에 실패했습니다. IP: 211.55.34.5, PORT: 443 방향의 Outbound 포트가 열려 있는지 확인해 주세요."),
    SERVER_ERROR("서버 처리중 에러가 발생 했습니다. 관리자에게 문의하세요.");

    ;

    private final String message;

    CodefError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
