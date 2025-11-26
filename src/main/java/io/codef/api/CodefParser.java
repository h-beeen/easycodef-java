package io.codef.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefException;

import java.util.List;

import static io.codef.api.constants.CodefConstant.DATA;
import static io.codef.api.constants.CodefConstant.RESULT;
import static io.codef.api.error.CodefError.PARSE_ERROR;

public class CodefParser {

    private CodefParser() {}

    protected static EasyCodefResponse.Result parseResult(JSONObject jsonResponse) {
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

    protected static Object parseData(JSONObject jsonResponse) {
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
