package io.codef.api.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpRequestBuilder(String url) {
        this.url = url;
    }

    public HttpRequestBuilder header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpRequestBuilder body(String body) {
        this.body = body;
        return this;
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
}
