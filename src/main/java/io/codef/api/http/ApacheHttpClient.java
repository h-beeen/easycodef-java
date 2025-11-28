package io.codef.api.http;

import io.codef.api.dto.HttpResponse;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApacheHttpClient implements HttpClient {

    @Override
    public HttpResponse postJson(HttpUriRequest request) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody =
                        response.getEntity() == null ? "" :
                                EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new HttpResponse(statusCode, responseBody);
            }
        } catch (IOException e) {
            return new HttpResponse(-1, e.getMessage());
        }
    }
}
