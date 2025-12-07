package io.codef.api.constant;

/**
 * CODEF 공통 문자열 enum 클래스
 *
 * @author kdso10
 * @since Dec 5, 2025
 */
public enum CodefConstant {
	RESULT("result"),
	DATA("data"),
	PATH_PREFIX("/v1"),
	RSA("RSA"),
	APPLICATION_JSON("application/json"),
	IS_2WAY("is2Way"),
	INFO_KEY("twoWayInfo");

	private final String value;

	CodefConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
