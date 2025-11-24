package io.codef.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.ApacheHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EasyCodefConnector {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = new ApacheHttpClient();

    private static EasyCodefResponse execute(String url, Map<String, String> headers, String body, boolean isTokenRequest) {
        try {
            HttpResponse httpResponse = httpClient.postJson(url, headers, body);

            if (httpResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
                EasyCodefError error = EasyCodefError.fromHttpStatus(httpResponse.getStatusCode());
                return ResponseHandler.fromError(error, url);
            }

            String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8);
            Map<String, Object> responseMap = mapper.readValue(
                    decoded,
                    new TypeReference<Map<String, Object>>() {}
            );

            return isTokenRequest
                    ? ResponseHandler.fromTokenResponse(responseMap)
                    : ResponseHandler.fromRawMap(responseMap);
        } catch (JsonProcessingException e) {
            return ResponseHandler.fromError(EasyCodefError.INVALID_JSON, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseHandler.fromError(EasyCodefError.UNSUPPORTED_ENCODING, e.getMessage());
        } catch (IOException e) {
            return ResponseHandler.fromError(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
    }

    protected static EasyCodefResponse publishToken(String oauthToken) {
        String url = CodefHost.OAUTH_DOMAIN + CodefPath.GET_TOKEN;
        Map<String, String> headers = EasyCodefUtil.createAuthorizationHeaders(oauthToken);
        return execute(url, headers, null, true);
    }

    protected static EasyCodefResponse requestProduct(String urlPath, String accessToken, Map<String, Object> bodyMap) {
        String jsonString = JSON.toJSONString(bodyMap);
        Map<String, String> headers = EasyCodefUtil.createBearerTokenHeaders(accessToken);
        return execute(urlPath, headers, jsonString, false);
    }
}
