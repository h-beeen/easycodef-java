package io.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.CodefException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.error.CodefError.*;
import static io.codef.api.util.JsonUtil.mapTypeRef;
import static io.codef.api.util.JsonUtil.mapper;

public class ResponseHandler {

    private ResponseHandler() {}

    protected static EasyCodefResponse processResponse(HttpResponse httpResponse) {
        try {
            String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8.name());
            Map<String, Object> responseMap = mapper().readValue(decoded, mapTypeRef());

            return responseMap.containsKey(ACCESS_TOKEN)
                    ? handleTokenResponse(responseMap)
                    : handleProductResponse(responseMap);

        } catch (UnsupportedEncodingException e) {
            throw CodefException.of(UNSUPPORTED_ENCODING, e.getMessage());
        } catch (JsonProcessingException e) {
            throw CodefException.of(INVALID_JSON, e.getMessage());
        }
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
