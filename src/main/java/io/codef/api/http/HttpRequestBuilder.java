package io.codef.api.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {

	private final Map<String, String> headers = new HashMap<>();

	private String url;
	private String body;
	private Integer timeout;

	public static HttpRequestBuilder builder() {
		return new HttpRequestBuilder();
	}

	public HttpRequestBuilder url(String url) {
		this.url = url;
		return this;
	}

	public HttpRequestBuilder header(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public HttpRequestBuilder body(String body) {
		this.body = body;
		return this;
	}

	public HttpRequestBuilder timeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	public CodefHttpRequest build() {
		return new CodefHttpRequest(url, headers, body, timeout);
	}
}
