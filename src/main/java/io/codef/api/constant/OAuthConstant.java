package io.codef.api.constant;

/**
 * CODEF OAuth 인증 응답 처리 필드 키 enum 클래스
 *
 */
public enum OAuthConstant {
	ACCESS_TOKEN("access_token"),
	EXPIRES_IN("expires_in");

	private final String value;

	OAuthConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
