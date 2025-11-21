package io.codef.api;

import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClientBuilder;

public class EasyCodefConnector {
	private static final ObjectMapper mapper = new ObjectMapper();
    private static final EasyCodefHttpClient httpClient = new ApacheEasyCodefHttpClient();

    private EasyCodefConnector() {}

	private static Map<String, Object> execute(String url, Map<String, String> headers, String body) {
        try {
            EasyCodefHttpResponse httpResponse = httpClient.postJson(url, headers, body);

            int statusCode = httpResponse.getStatusCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return buildErrorResponse(statusCode);
            }

            String responseBody = httpResponse.getBody();
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
        String jsonString = JSON.toJSONString(bodyMap);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json");

        Map<String, Object> response = execute(urlPath, headers, jsonString);
        return new EasyCodefResponse(response);
    }

	protected static Map<String, Object> publishToken(String oauthToken) {
        String url = EasyCodefConstant.OAUTH_DOMAIN + EasyCodefConstant.GET_TOKEN;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", oauthToken);

        return execute(url, headers, null);
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
