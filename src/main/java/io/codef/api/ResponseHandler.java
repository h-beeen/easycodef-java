package io.codef.api;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.codef.api.constants.CodefConstant.*;
import static io.codef.api.error.CodefError.*;

public class ResponseHandler {

    private ResponseHandler() {}

    protected static EasyCodefResponse processResponse(HttpResponse httpResponse) {
        try {
            String decoded = URLDecoder.decode(httpResponse.getBody(), StandardCharsets.UTF_8.name());

            return decoded.contains(ACCESS_TOKEN)
                    ? handleTokenResponse(decoded)
                    : handleProductResponse(decoded);

        } catch (UnsupportedEncodingException e) {
            throw CodefException.of(UNSUPPORTED_ENCODING, e.getMessage());
        }
    }

    private static EasyCodefResponse handleTokenResponse(String responseBody) {
        JSONObject jsonResponse = JSON.parseObject(responseBody);

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", jsonResponse.getString("access_token"));
        tokenMap.put("expires_in", jsonResponse.getString("expires_in"));

        return new EasyCodefResponse(null, tokenMap);
    }

    private static EasyCodefResponse handleProductResponse(String responseBody) {
        JSONObject jsonResponse = JSON.parseObject(responseBody);

        EasyCodefResponse.Result result = parseResult(jsonResponse);
        Object data = parseData(jsonResponse);

        return new EasyCodefResponse(result, data);
    }

    private static EasyCodefResponse.Result parseResult(JSONObject jsonResponse) {
        JSONObject resultObj = jsonResponse.getJSONObject(RESULT);

        if (resultObj == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        EasyCodefResponse.Result result = resultObj.to(EasyCodefResponse.Result.class);

        if (result == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        return result;
    }

    private static Object parseData(JSONObject jsonResponse) {
        try {
            return parseObjectData(jsonResponse);
        } catch (Exception e) {
            return parseArrayData(jsonResponse);
        }
    }

    private static Object parseObjectData(JSONObject jsonResponse) {
        JSONObject dataObj = jsonResponse.getJSONObject(DATA);

        if (dataObj == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        Object data = dataObj.to(Object.class);
        if (data == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        return data;
    }

    private static List<?> parseArrayData(JSONObject jsonResponse) {
        JSONArray dataArr = jsonResponse.getJSONArray(DATA);

        if (dataArr == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        List<?> list = dataArr.to(List.class);
        if (list == null) {
            throw CodefException.from(PARSE_ERROR);
        }

        return list;
    }
}
