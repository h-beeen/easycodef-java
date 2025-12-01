package io.codef.api.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

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

	public HttpPost build() {
		HttpPost httpPost = new HttpPost(url);

		headers.forEach(httpPost::setHeader);

		if (body != null && !body.isEmpty()) {
			httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
		}

		if (timeout != null && timeout > 0) {
			RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(Timeout.ofSeconds(timeout))
				.setResponseTimeout(Timeout.ofSeconds(timeout))
				.build();
			httpPost.setConfig(config);
		}

		return httpPost;
	}
}
