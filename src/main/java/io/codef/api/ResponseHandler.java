package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.EasyCodefError;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.error.EasyCodefError.*;
import static io.codef.api.util.JsonUtil.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseHandler {

    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    private ResponseHandler() {}

    protected static EasyCodefResponse processResponse(HttpResponse httpResponse) {
        if (httpResponse.getStatusCode() != HTTP_OK) {
            EasyCodefError error = fromHttpStatus(httpResponse.getStatusCode());
            return handleErrorResponse(error);
        }

        try {
            String decoded = URLDecoder.decode(httpResponse.getBody(), UTF_8.name());
            Map<String, Object> responseMap = mapper().readValue(decoded, mapTypeRef());

            return responseMap.containsKey(ACCESS_TOKEN)
                    ? handleTokenResponse(responseMap)
                    : handleProductResponse(responseMap);

        } catch (UnsupportedEncodingException e) {
            return handleErrorResponse(UNSUPPORTED_ENCODING, e.getMessage());
        } catch (JsonProcessingException e) {
            return handleErrorResponse(INVALID_JSON, e.getMessage());
        } catch (Exception e) {
            return handleErrorResponse(LIBRARY_SENDER_ERROR, e.getMessage());
        }
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

    private static EasyCodefResponse handleTokenResponse(Map<String, Object> tokenMap) {
        return new EasyCodefResponse(null, tokenMap);
    }

    private static EasyCodefResponse handleProductResponse(Map<String, Object> map) {
        EasyCodefResponse.Result result = parseResult(map.get(RESULT));
        Object data = map.get(DATA);

        return new EasyCodefResponse(result, data);
    }

    private static EasyCodefResponse.Result parseResult(Object resultObj) {
        if (!(resultObj instanceof Map)) {
            return null;
        }

        try {
            Map<String, Object> resultMap = mapper().convertValue(resultObj, mapTypeRef());
            return new EasyCodefResponse.Result(
                    getStringValue(resultMap, CODE),
                    getStringValue(resultMap, EXTRA_MESSAGE),
                    getStringValue(resultMap, MESSAGE),
                    getStringValue(resultMap, TRANSACTION_ID)
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
