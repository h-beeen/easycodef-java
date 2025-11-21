package io.codef.api;

import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class EasyCodefConnector {
	private static final ObjectMapper mapper = new ObjectMapper();

	private static Map<String, Object> execute(HttpPost httpPost) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return buildErrorResponse(statusCode);
            }

            String responseBody =
                    response.getEntity() == null ? "" : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            String decoded = URLDecoder.decode(responseBody, StandardCharsets.UTF_8.name());

            return mapper.readValue(
                    decoded,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            return new EasyCodefResponse(
                    EasyCodefMessageConstant.LIBRARY_SENDER_ERROR,
                    e.getMessage()
            );
        }
	}

	protected static EasyCodefResponse requestProduct(String urlPath, String accessToken, Map<String, Object> bodyMap) {
        HttpPost httpPost = new HttpPost(urlPath);

        httpPost.addHeader("Authorization", "Bearer " + accessToken);

        String jsonString = JSON.toJSONString(bodyMap);

        httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));

        Map<String, Object> response = execute(httpPost);

        return new EasyCodefResponse(response);
    }

	protected static Map<String, Object> publishToken(String oauthToken) {
        HttpPost httpPost = new HttpPost(EasyCodefConstant.OAUTH_DOMAIN + EasyCodefConstant.GET_TOKEN);
        httpPost.addHeader("Authorization", oauthToken);

        return execute(httpPost);
	}

    private static EasyCodefResponse buildErrorResponse(int responseCode) {
        EasyCodefMessageConstant messageConstant;

        switch (responseCode) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                messageConstant = EasyCodefMessageConstant.BAD_REQUEST;
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                messageConstant = EasyCodefMessageConstant.UNAUTHORIZED;
                break;
            case HttpURLConnection.HTTP_FORBIDDEN:
                messageConstant = EasyCodefMessageConstant.FORBIDDEN;
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                messageConstant = EasyCodefMessageConstant.NOT_FOUND;
                break;
            default:
                messageConstant = EasyCodefMessageConstant.SERVER_ERROR;
        }

        return new EasyCodefResponse(messageConstant);
    }
}
