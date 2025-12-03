package io.codef.api.constant;

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
