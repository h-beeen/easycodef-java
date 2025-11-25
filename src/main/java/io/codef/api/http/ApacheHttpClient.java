package io.codef.api.http;

import io.codef.api.dto.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApacheHttpClient implements HttpClient {

    @Override
    public HttpResponse postJson(
            String url,
            Map<String, String> headers,
            String body
    ) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(url);

            headers.forEach(httpPost::setHeader);

            if (body != null && !body.isEmpty()) {
                httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
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
