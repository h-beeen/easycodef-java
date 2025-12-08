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

/**
 * HTTP 응답을 {@link EasyCodefResponse}로 변환하는 유틸리티 클래스
 *
 */
public class ResponseHandler {

	private ResponseHandler() {
	}

	/**
	 * HTTP 응답을 {@link EasyCodefResponse}로 변환하여 반환
	 *
	 * <p>
	 *     응답 JSON에 {@code access_token} 키가 존재하면 토큰 응답으로 간주하고 <br>
	 *     {@link #handleTokenResponse(JSONObject)}로 처리 <br>
	 *     그렇지 않으면 상품 응답으로 간주하고 <br>
	 *     {@link #handleProductResponse(JSONObject)}로 처리
	 * </p>
	 *
	 * @param httpResponse 수신한 원본 HTTP 응답 문자열
	 * @return 변환된 {@link EasyCodefResponse} 객체
	 */
	public static EasyCodefResponse processResponse(String httpResponse) {
		String decoded = UrlUtil.decode(httpResponse);
		JSONObject jsonObject = JSON.parseObject(decoded);

		return jsonObject.containsKey(ACCESS_TOKEN.getValue())
			? handleTokenResponse(jsonObject)
			: handleProductResponse(jsonObject);
	}

	/**
	 * Access Token이 포함된 OAuth 응답 처리
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return 토큰 정보를 포함하는 {@link EasyCodefResponse}
	 */
	private static EasyCodefResponse handleTokenResponse(JSONObject jsonResponse) {
		return EasyCodefResponse.from(jsonResponse);
	}

	/**
	 * 상품 API 응답 처리
	 *
	 * <p>
	 *     {@code result}, {@code data}, 나머지 필드를 파싱하여 <br>
	 *     {@link EasyCodefResponse}의 각 필드로 매핑
	 * </p>
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return 파싱된 {@link EasyCodefResponse}
	 */
	private static EasyCodefResponse handleProductResponse(JSONObject jsonResponse) {
		EasyCodefResponse.Result result = parseResult(jsonResponse);
		Object data = parseData(jsonResponse);
		Object extraInfo = parseExtraInfo(jsonResponse);

		return EasyCodefResponse.of(result, data, extraInfo);
	}

	/**
	 * 응답 JSON에서 {@code result} 필드 파싱
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return {@link EasyCodefResponse.Result} 인스턴스
	 * @throws CodefException {@code result} 필드가 없거나 파싱에 실패한 경우 {@link CodefError#PARSE_ERROR}
	 */
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

	/**
	 * 응답 JSON에서 {@code data} 필드 파싱
	 *
	 * <p>
	 *     {@code data}가 Object인 경우와 Array인 경우를 모두 지원 <br>
	 *     Object 파싱을 시도하고, 실패 시 Array 파싱 시도
	 * </p>
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return {@code data} 필드에 해당하는 객체 또는 리스트
	 */
	private static Object parseData(JSONObject jsonResponse) {
		try {
			return parseObjectData(jsonResponse);
		} catch (Exception e) {
			return parseArrayData(jsonResponse);
		}
	}

	/**
	 * {@code data} 필드를 JSON 객체로 파싱
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return {@code data}에 해당하는 {@link JSONObject}
	 * @throws CodefException {@code data} 필드가 없거나 Object 형태가 아닌 경우 {@link CodefError#PARSE_ERROR}
	 */
	private static Object parseObjectData(JSONObject jsonResponse) {
		JSONObject dataObj = jsonResponse.getJSONObject(DATA.getValue());

		if (dataObj == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		return dataObj;
	}

	/**
	 * {@code data} 필드를 JSON 배열로 파싱
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return {@code data}에 해당하는 {@link JSONArray} 리스트 표현
	 * @throws CodefException {@code data} 필드가 없거나 Array 형태가 아닌 경우 {@link CodefError#PARSE_ERROR}
	 */
	private static List<?> parseArrayData(JSONObject jsonResponse) {
		JSONArray dataArr = jsonResponse.getJSONArray(DATA.getValue());

		if (dataArr == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		return dataArr;
	}

	/**
	 * 응답 JSON에서 {@code result}, {@code data} 이외의 필드가 존재하면, 추가 정보로 반환
	 *
	 * @param jsonResponse JSON 파싱된 응답 객체
	 * @return 추가 정보가 담긴 {@link JSONObject}
	 */
	private static Object parseExtraInfo(JSONObject jsonResponse) {
		JSONObject extraInfo = new JSONObject(jsonResponse);

		extraInfo.remove(RESULT.getValue());
		extraInfo.remove(DATA.getValue());

		return extraInfo;
	}
}
