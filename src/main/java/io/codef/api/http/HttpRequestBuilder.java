package io.codef.api.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

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

    public HttpUriRequest build() {
        HttpPost httpPost = new HttpPost(url);

        headers.forEach(httpPost::setHeader);

        if (body != null && !body.isEmpty()) {
            httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }

        if (timeout != null && timeout > 0) {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000)
                    .build();
            httpPost.setConfig(config);
        }

        return httpPost;
    }
}
