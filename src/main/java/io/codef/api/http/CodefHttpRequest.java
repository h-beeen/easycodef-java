package io.codef.api.http;

import java.util.Map;

/**
 * CODEF API 통신을 위한 HTTP 요청 정보 객체
 *
 */
public class CodefHttpRequest {
	private final String url;
	private final Map<String, String> headers;
	private final String body;
	private final Integer timeout;

	/**
	 * CodefHttpRequest 생성자
	 *
	 * @param url     요청 대상 URL
	 * @param headers HTTP 요청 헤더 (Map)
	 * @param body    HTTP 요청 바디 (Payload)
	 * @param timeout 요청 타임아웃 (초)
	 */
	public CodefHttpRequest(String url, Map<String, String> headers, String body, Integer timeout) {
		this.url = url;
		this.headers = headers;
		this.body = body;
		this.timeout = timeout;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public Integer getTimeout() {
		return timeout;
	}
}
