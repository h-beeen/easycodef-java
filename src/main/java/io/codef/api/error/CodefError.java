package io.codef.api.error;

/**
 * CODEF 라이브러리 오류 코드 enum 클래스
 *
 */
public enum CodefError {
	/** 문자열 디코딩 실패 */
	UNSUPPORTED_ENCODING(
		"The string is encoded in an unsupported format."),

	JSON_PARSE_ERROR(
		"An error occurred during JSON deserialization."),

	/** 응답 JSON 형식이 예상 구조와 일치하지 않을 때 */
	PARSE_ERROR(
		"The client failed to parse the server response in the expected format. Please verify the request format."),

	/** RSA 암호화 수행 중 오류 발생 */
	RSA_ENCRYPTION_ERROR(
		"An error occurred during RSA encryption. Please verify the PublicKey value."),

	/** Two-Way 요청에 필요한 필수 필드가 없거나 잘못된 경우 */
	INVALID_2WAY_INFO(
		"Invalid information for 2-way request processing. "
			+ "Items received in the response must be included exactly in the 2-way request."),
	/** Two-Way 요청이 아닌데 관련 키워드를 포함하고 요청했을 경우 */
	INVALID_2WAY_KEYWORD(
		"Requests for additional authentication (2-way) must use the requestCertification method."),
	/** 상품 URL 형식 오류 */
	INVALID_PATH_REQUESTED(
		"Invalid path requested. Please exclude the domain address and provide only the relative path (e.g., /v1/kr/...)."),
	INVALID_SERVICE_TYPE(
		"The requested service type is not supported."),

	/** 서비스 환경이 지정되지 않음 */
	EMPTY_SERVICE_TYPE(
		"A membership version is required for the product request. Please set the desired membership version."),
	/** Client ID 누락 */
	EMPTY_CLIENT_ID(
		"Client ID is required for the product request. Please set the Client ID."),
	/** Client Secret 누락 */
	EMPTY_CLIENT_SECRET(
		"Client Secret is required for the product request. Please set the Client Secret."),
	/** Public Key 누락 */
	EMPTY_PUBLIC_KEY(
		"A public key is required for the product request. Please set the public key information."),
	/** 경로 값이 비어 있음 */
	EMPTY_PATH(
		"A path is required for the product request. Please set the path information."),
	/** 요청 파라미터가 비어 있거나 존재하지 않을 경우 */
	EMPTY_PARAMETER(
		"Parameter is required for the product request. Please set the parameter information."),
	EMPTY_EASYCODEF_REQUEST(
		"EasyCodefRequest is required for the product request. Please set the EasyCodefRequest parameter information."),

	UNAUTHORIZED(
		"You do not have permission to make this request. Please check your access token or permissions."),

	INTERNAL_SERVER_ERROR(
		"The server returned an unsuccessful response. Please check the error details from the server."),

	EMPTY_CODEF_RESPONSE(
		"the server returned an empty response body."),

	/** 입출력(통신) 오류 */
	IO_ERROR(
		"An error occurred because the request was either not sent properly or not received. "
			+ "Please check if the outbound port to IP: 211.55.34.5, PORT: 443 is open."),

	OAUTH_ERROR(
		"OAuth authentication failed. Please check your client_id, client_secret, or validity of the access token."),

	/** 서버 응답 지연으로 인한 타임아웃 */
	TIMEOUT_ERROR(
		"The request timed out. The server did not respond within the expected time. "
			+ "Please check the timeout settings or network connectivity.");

	private final String message;

	CodefError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
