package io.codef.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.EasyCodefError;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class ResponseHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    protected static EasyCodefResponse processResponse(
            HttpResponse httpResponse,
            boolean isTokenRequest
    ) throws IOException {
        if (httpResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            EasyCodefError error = EasyCodefError.fromHttpStatus(httpResponse.getStatusCode());
            return ResponseHandler.fromError(error);
        }

        String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8.name());
        Map<String, Object> responseMap = mapper.readValue(
                decoded,
                new TypeReference<Map<String, Object>>() {
                }
        );

        return isTokenRequest
                ? ResponseHandler.fromTokenResponse(responseMap)
                : ResponseHandler.fromRawMap(responseMap);
    }

    public static EasyCodefResponse fromTokenResponse(Map<String, Object> tokenMap) {
        if (tokenMap == null || tokenMap.isEmpty()) {
            return fromError(EasyCodefError.LIBRARY_SENDER_ERROR, "Empty token response");
        }

        return new EasyCodefResponse(null, tokenMap);
    }

    @SuppressWarnings("unchecked")
    public static EasyCodefResponse fromRawMap(Map<String, Object> map) {
        if (map == null) {
            return new EasyCodefResponse(null, null);
        }

        Map<String, Object> resultMap = null;
        Object data = null;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (EasyCodefConstant.RESULT.equals(key)) {
                resultMap = (Map<String, Object>) value;
            } else if (EasyCodefConstant.DATA.equals(key)) {
                data = value;
            }
        }

        EasyCodefResponse.Result result = null;
        if (resultMap != null) {
            result = new EasyCodefResponse.Result(
                    (String) resultMap.get(EasyCodefConstant.CODE),
                    (String) resultMap.get(EasyCodefConstant.EXTRA_MESSAGE),
                    (String) resultMap.get(EasyCodefConstant.MESSAGE),
                    (String) resultMap.get(EasyCodefConstant.TRANSACTION_ID)
            );
        }

        return new EasyCodefResponse(result, data);
    }

    public static EasyCodefResponse fromError(EasyCodefError error) {
        return fromError(error, null);
    }

    public static EasyCodefResponse fromError(EasyCodefError error, String extraMessage) {
        if (error == null) {
            return new EasyCodefResponse(null, Collections.<String, Object>emptyMap());
        }

        String resolvedExtraMessage =
                (extraMessage != null && !extraMessage.isEmpty())
                        ? extraMessage
                        : "";

        EasyCodefResponse.Result result = new EasyCodefResponse.Result(
                error.getCode(),
                resolvedExtraMessage,
                error.getMessage(),
                null
        );

        return new EasyCodefResponse(result, Collections.<String, Object>emptyMap());
    }
}
