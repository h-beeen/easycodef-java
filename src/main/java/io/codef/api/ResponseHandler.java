package io.codef.api;

import static io.codef.api.constants.CodefConstant.*;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.dto.HttpResponse;
import io.codef.api.util.URLUtil;

public class ResponseHandler {

    private ResponseHandler() {}

    protected static EasyCodefResponse processResponse(HttpResponse httpResponse) {
        String decoded = URLUtil.decode(httpResponse.getBody());
        JSONObject jsonObject = JSON.parseObject(decoded);

        return jsonObject.containsKey(ACCESS_TOKEN)
                ? handleTokenResponse(jsonObject)
                : handleProductResponse(jsonObject);
    }

    private static EasyCodefResponse handleTokenResponse(JSONObject jsonResponse) {
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, jsonResponse.getString(ACCESS_TOKEN));
        tokenMap.put(EXPIRES_IN, jsonResponse.getString(EXPIRES_IN));

        return new EasyCodefResponse(null, tokenMap);
    }

    private static EasyCodefResponse handleProductResponse(JSONObject jsonResponse) {
        EasyCodefResponse.Result result = CodefParser.parseResult(jsonResponse);
        Object data = CodefParser.parseData(jsonResponse);

        return new EasyCodefResponse(result, data);
    }
}
