package io.codef.api.handler;

import static io.codef.api.constant.CodefConstant.*;
import static io.codef.api.constant.OAuthConstant.*;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.util.UrlUtil;

public class ResponseHandler {

	private ResponseHandler() {
	}

	public static EasyCodefResponse processResponse(String httpResponse) {
		String decoded = UrlUtil.decode(httpResponse);
		JSONObject jsonObject = JSON.parseObject(decoded);

		return jsonObject.containsKey(ACCESS_TOKEN.getValue())
			? handleTokenResponse(jsonObject)
			: handleProductResponse(jsonObject);
	}

	private static EasyCodefResponse handleTokenResponse(JSONObject jsonResponse) {
		return EasyCodefResponse.from(jsonResponse);
	}

	private static EasyCodefResponse handleProductResponse(JSONObject jsonResponse) {
		EasyCodefResponse.Result result = parseResult(jsonResponse);
		Object data = parseData(jsonResponse);

		return EasyCodefResponse.of(result, data);
	}

	private static EasyCodefResponse.Result parseResult(JSONObject jsonResponse) {
		if (!jsonResponse.containsKey(RESULT.getValue())) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		EasyCodefResponse.Result result = jsonResponse.getObject(RESULT.getValue(), EasyCodefResponse.Result.class);

		if (result == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
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
		JSONObject dataObj = jsonResponse.getJSONObject(DATA.getValue());

		if (dataObj == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		return dataObj;
	}

	private static List<?> parseArrayData(JSONObject jsonResponse) {
		JSONArray dataArr = jsonResponse.getJSONArray(DATA.getValue());

		if (dataArr == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		return dataArr;
	}
}
