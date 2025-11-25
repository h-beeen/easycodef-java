package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.constants.CodefPath;
import io.codef.api.constants.CodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.EasyCodefError;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static io.codef.api.EasyCodefUtil.mapper;
import static io.codef.api.EasyCodefUtil.mapTypeRef;

public class ResponseHandler {

    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    protected static EasyCodefResponse processResponse(HttpResponse httpResponse, String url) {
        if (httpResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            EasyCodefError error = EasyCodefError.fromHttpStatus(httpResponse.getStatusCode());
            return handleErrorResponse(error);
        }

        try {
            String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8.name());
            Map<String, Object> responseMap = mapper().readValue(decoded, mapTypeRef());

            return isTokenRequest(url) ? handleTokenResponse(responseMap) : handleProductResponse(responseMap);

        } catch (UnsupportedEncodingException e) {
            return handleErrorResponse(EasyCodefError.UNSUPPORTED_ENCODING, e.getMessage());
        } catch (JsonProcessingException e) {
            return handleErrorResponse(EasyCodefError.INVALID_JSON, e.getMessage());
        } catch (Exception e) {
            return handleErrorResponse(EasyCodefError.LIBRARY_SENDER_ERROR, e.getMessage());
        }
    }

    private static boolean isTokenRequest(String url) {
        return url.contains(CodefPath.GET_TOKEN);
    }

    private static EasyCodefResponse handleTokenResponse(Map<String, Object> tokenMap) {
        return new EasyCodefResponse(null, tokenMap);
    }

    private static EasyCodefResponse handleProductResponse(Map<String, Object> map) {
        EasyCodefResponse.Result result = parseResult(map.get(CodefConstant.RESULT));
        Object data = map.get(CodefConstant.DATA);

        return new EasyCodefResponse(result, data);
    }


    protected static EasyCodefResponse handleErrorResponse(EasyCodefError error) {
        return handleErrorResponse(error, "");
    }

    protected static EasyCodefResponse handleErrorResponse(EasyCodefError error, String extraMessage) {
        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),
                extraMessage,
                error.getMessage(),
                null
        );

        return new EasyCodefResponse(result, EMPTY_MAP);
    }

    private static EasyCodefResponse.Result parseResult(Object resultObj) {
        if (!(resultObj instanceof Map)) {
            return null;
        }

        try {
            Map<String, Object> resultMap = mapper().convertValue(resultObj, mapTypeRef());
            return new EasyCodefResponse.Result(
                    getStringValue(resultMap, CodefConstant.CODE),
                    getStringValue(resultMap, CodefConstant.EXTRA_MESSAGE),
                    getStringValue(resultMap, CodefConstant.MESSAGE),
                    getStringValue(resultMap, CodefConstant.TRANSACTION_ID)
            );
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? String.valueOf(value) : null;
    }
}
