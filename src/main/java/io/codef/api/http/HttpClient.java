package io.codef.api.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import io.codef.api.dto.HttpResponse;

public class HttpClient {

    private final CloseableHttpClient httpClient;

    public HttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpResponse postJson(HttpUriRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody =
                    response.getEntity() == null ? "" :
                            EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return new HttpResponse(statusCode, responseBody);
        } catch (IOException e) {
            return new HttpResponse(-1, e.getMessage());
        }
    }
}
