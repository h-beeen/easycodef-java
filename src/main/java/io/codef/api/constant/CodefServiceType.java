package io.codef.api.constant;

/**
 * CODEF 서비스 타입 enum 클래스
 *
 * @author kdso10
 * @since Dec 5, 2025
 */
public enum CodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);

	private final String host;

	CodefServiceType(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
}
