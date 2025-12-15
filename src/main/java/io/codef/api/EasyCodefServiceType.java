package io.codef.api;

import io.codef.api.constant.CodefHost;

/**
 * CODEF 서비스 타입 enum 클래스
 *
 */
public enum EasyCodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);

	private final String host;

	EasyCodefServiceType(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
}
