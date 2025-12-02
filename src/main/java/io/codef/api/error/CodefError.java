package io.codef.api.error;

public enum CodefError {
	UNSUPPORTED_ENCODING(
		"The string is encoded in an unsupported format."
	),

	PARSE_ERROR(
		"The client failed to parse the server response in the expected format. Please verify the request format."
	),

	RSA_ENCRYPTION_ERROR(
		"An error occurred during RSA encryption. Please verify the PublicKey value."
	),

	INVALID_2WAY_INFO(
		"Invalid information for 2-way request processing. Items received in the response must be included exactly in the 2-way request."
	),
	INVALID_2WAY_KEYWORD(
		"Requests for additional authentication (2-way) must use the requestCertification method."
	),

	INVALID_PATH_REQUESTED(
		"The path should be requested in the following format: `/v1/kr/***/***/...`"
	),

	EMPTY_SERVICE_TYPE(
		"A membership version is required for the product request. Please set the desired membership version."
	),
	EMPTY_CLIENT_ID(
		"Client ID is required for the product request. Please set the Client ID."
	),
	EMPTY_CLIENT_SECRET(
		"Client Secret is required for the product request. Please set the Client Secret."
	),
	EMPTY_PUBLIC_KEY(
		"A public key is required for the product request. Please set the public key information."
	),

	EMPTY_PATH(
		"A path is required for the product request. Please set the path information."
	),
	EMPTY_PARAMETER(
		"Parameter is required for the product request. Please set the parameter information."
	),

	IO_ERROR(
		"An error occurred because the request was either not sent properly or not received. Please check if the outbound port to IP: 211.55.34.5, PORT: 443 is open."
	),

	TIMEOUT_ERROR(
		"The request timed out. The server did not respond within the expected time. Please check the timeout settings or network connectivity."
	);

	private final String message;

	CodefError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
