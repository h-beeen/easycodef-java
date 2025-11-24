package io.codef.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;
import io.codef.api.dto.HttpResponse;
import io.codef.api.http.HttpClient;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.http.HttpRequestBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class EasyCodefConnector {

    private static final HttpClient httpClient = new ApacheHttpClient();

    private static HttpRequestBuilder request(String url) {
        return new HttpRequestBuilder(url);
    }

    public static EasyCodefResponse execute(HttpRequestBuilder builder) {
        try {
            HttpResponse httpResponse = httpClient.postJson(
                    builder.getUrl(),
                    builder.getHeaders(),
                    builder.getBody()
            );
            return ResponseHandler.processResponse(httpResponse, builder.isTokenRequest());

        } catch (JsonProcessingException e) {
            return ResponseHandler.fromError(EasyCodefError.INVALID_JSON, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseHandler.fromError(EasyCodefError.UNSUPPORTED_ENCODING, e.getMessage());
        } catch (IOException e) {
            return ResponseHandler.fromError(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
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
