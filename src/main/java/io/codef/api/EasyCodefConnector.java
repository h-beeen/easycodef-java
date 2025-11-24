package io.codef.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.EasyCodefHttpClient;

import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EasyCodefConnector {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = new EasyCodefHttpClient();

    private EasyCodefConnector() {
    }

    private static Map<String, Object> execute(
            String url,
            Map<String, String> headers,
            String body
    ) {
        try {
            HttpResponse httpResponse = httpClient.postJson(url, headers, body);

            int statusCode = httpResponse.getStatusCode();
            String responseBody = httpResponse.getBody();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                EasyCodefError error = EasyCodefError.fromHttpStatus(statusCode);
                EasyCodefResponse errorResponse =
                        ResponseHandler.fromError(error, url);

                return ResponseHandler.toMap(errorResponse);
            }

            String decoded = URLDecoder.decode(responseBody, StandardCharsets.UTF_8.name());

            return mapper.readValue(
                    decoded,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            EasyCodefResponse errorResponse =
                    ResponseHandler.fromError(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());

            return ResponseHandler.toMap(errorResponse);
        }
    }

    protected static Map<String, Object> publishToken(String oauthToken) {
        String url = CodefHost.OAUTH_DOMAIN + CodefPath.GET_TOKEN;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", oauthToken);

        return execute(url, headers, null);
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        String jsonString = JSON.toJSONString(bodyMap);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json");

        Map<String, Object> response = execute(urlPath, headers, jsonString);
        return ResponseHandler.fromRawMap(response);
    }
}
