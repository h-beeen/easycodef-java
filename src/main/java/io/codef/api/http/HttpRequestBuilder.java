package io.codef.api.http;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.EasyCodefConnector;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    private boolean isTokenRequest = false;

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

    public HttpRequestBuilder asTokenRequest() {
        this.isTokenRequest = true;
        return this;
    }

    public EasyCodefResponse execute() {
        return EasyCodefConnector.execute(this);
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

    public boolean isTokenRequest() {
        return isTokenRequest;
    }
}
