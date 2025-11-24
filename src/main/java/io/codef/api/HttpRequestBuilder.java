package io.codef.api;

import io.codef.api.dto.EasyCodefResponse;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    private boolean isTokenRequest = false;

    HttpRequestBuilder(String url) {
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

    String getUrl() {
        return url;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    String getBody() {
        return body;
    }

    boolean isTokenRequest() {
        return isTokenRequest;
    }
}
