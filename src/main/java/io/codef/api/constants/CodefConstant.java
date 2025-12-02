package io.codef.api.constants;

public enum CodefConstant {
	RESULT("result"),
	DATA("data"),
	PATH_PREFIX("/v1"),
	RSA("RSA"),
	APPLICATION_JSON("application/json");

	private final String value;

	CodefConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
