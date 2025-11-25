package io.codef.api.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private final Map<String, String> headers = new HashMap<>();
    private String url;
    private String body;

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
