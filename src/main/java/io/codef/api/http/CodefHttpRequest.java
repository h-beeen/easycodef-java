package io.codef.api.http;

import java.util.Map;

public class CodefHttpRequest {
	private final String url;
	private final Map<String, String> headers;
	private final String body;
	private final Integer timeout;

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
