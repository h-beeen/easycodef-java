package io.codef.api.handler;

import static io.codef.api.constants.CodefConstant.OAuth.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.HttpResponse;
import io.codef.api.util.URLUtil;

public class ResponseHandler {

    private ResponseHandler() {}

    public static EasyCodefResponse processResponse(HttpResponse httpResponse) {
        String decoded = URLUtil.decode(httpResponse.getBody());
        JSONObject jsonObject = JSON.parseObject(decoded);

        return jsonObject.containsKey(ACCESS_TOKEN)
                ? handleTokenResponse(jsonObject)
                : handleProductResponse(jsonObject);
    }

    private static EasyCodefResponse handleTokenResponse(JSONObject jsonResponse) {
        return new EasyCodefResponse(null, jsonResponse);
    }

    private static EasyCodefResponse handleProductResponse(JSONObject jsonResponse) {
        EasyCodefResponse.Result result = CodefParser.parseResult(jsonResponse);
        Object data = CodefParser.parseData(jsonResponse);

        return new EasyCodefResponse(result, data);
    }
}
