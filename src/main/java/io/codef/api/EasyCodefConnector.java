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

    private EasyCodefConnector() {
    }

    private static HttpRequestBuilder request(String url) {
        return new HttpRequestBuilder(url);
    }

    static EasyCodefResponse execute(HttpRequestBuilder builder) {
        try {
            HttpResponse httpResponse = httpClient.postJson(
                    builder.getUrl(),
                    builder.getHeaders(),
                    builder.getBody()
            );
            return processResponse(httpResponse, builder.isTokenRequest());

        } catch (JsonProcessingException e) {
            return ResponseHandler.fromError(EasyCodefError.INVALID_JSON, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseHandler.fromError(EasyCodefError.UNSUPPORTED_ENCODING, e.getMessage());
        } catch (IOException e) {
            return ResponseHandler.fromError(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
    }

    private static EasyCodefResponse processResponse(
            HttpResponse httpResponse,
            boolean isTokenRequest
    ) throws IOException {
        if (httpResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            EasyCodefError error = EasyCodefError.fromHttpStatus(httpResponse.getStatusCode());
            return ResponseHandler.fromError(error);
        }

        String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8);
        Map<String, Object> responseMap = mapper.readValue(
                decoded,
                new TypeReference<Map<String, Object>>() {
                }
        );

        return isTokenRequest
                ? ResponseHandler.fromTokenResponse(responseMap)
                : ResponseHandler.fromRawMap(responseMap);
    }

    protected static EasyCodefResponse publishToken(String oauthToken) {
        return request(CodefHost.OAUTH_DOMAIN + CodefPath.GET_TOKEN)
                .header("Authorization", oauthToken)
                .asTokenRequest()
                .execute();
    }

    protected static EasyCodefResponse requestProduct(
            String urlPath,
            String accessToken,
            Map<String, Object> bodyMap
    ) {
        return request(urlPath)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(JSON.toJSONString(bodyMap))
                .execute();
    }
}
