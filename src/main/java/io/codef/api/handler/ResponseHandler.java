package io.codef.api.handler;

import static io.codef.api.constant.CodefConstant.*;
import static io.codef.api.constant.OAuthConstant.*;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.util.JsonUtil;
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
	 * @param httpResponse 수신한 원본 HTTP 응답 문자열
	 * @return 변환된 {@link EasyCodefResponse} 객체
	 */
	public static EasyCodefResponse processResponse(String httpResponse) {
		String decoded = UrlUtil.decode(httpResponse);
		JsonNode jsonNode = JsonUtil.fromJson(decoded, JsonNode.class);

		return jsonNode.has(ACCESS_TOKEN.getValue())
			? handleTokenResponse(jsonNode)
			: handleProductResponse(jsonNode);
	}

	/**
	 * Access Token이 포함된 OAuth 응답 처리
	 *
	 * @param jsonNode JSON 파싱된 응답 객체
	 * @return 토큰 정보를 포함하는 {@link EasyCodefResponse}
	 */
	private static EasyCodefResponse handleTokenResponse(JsonNode jsonNode) {
		Map<String, Object> jsonMap = JsonUtil.toMap(jsonNode);
		return EasyCodefResponse.from(jsonMap);
	}

	/**
	 * 상품 API 응답 처리
	 *
	 * <p>
	 *     {@code result}, {@code data}, 나머지 필드를 파싱하여 <br>
	 *     {@link EasyCodefResponse}의 각 필드로 매핑
	 * </p>
	 *
	 * @param jsonNode JSON 파싱된 응답 객체
	 * @return 파싱된 {@link EasyCodefResponse}
	 */
	private static EasyCodefResponse handleProductResponse(JsonNode jsonNode) {
		EasyCodefResponse.Result result = parseResult(jsonNode);
		Object data = parseData(jsonNode);
		Object extraInfo = parseExtraInfo(jsonNode);

		return EasyCodefResponse.of(result, data, extraInfo);
	}

	/**
	 * 응답 JSON에서 {@code result} 필드 파싱
	 *
	 * @param jsonNode JSON 파싱된 응답 객체
	 * @return {@link EasyCodefResponse.Result} 인스턴스
	 * @throws CodefException {@code result} 필드가 없거나 파싱에 실패한 경우 {@link CodefError#PARSE_ERROR}
	 */
	private static EasyCodefResponse.Result parseResult(JsonNode jsonNode) {
		if (!jsonNode.has(RESULT.getValue())) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		EasyCodefResponse.Result result = JsonUtil.convertValue(
			jsonNode.get(RESULT.getValue()),
			EasyCodefResponse.Result.class
		);

		if (result == null) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		return result;
	}

	private static Object parseData(JsonNode jsonNode) {
		JsonNode dataNode = jsonNode.get(DATA.getValue());

		if (dataNode == null || dataNode.isNull()) {
			throw CodefException.from(CodefError.PARSE_ERROR);
		}

		if (dataNode.isObject()) {
			return JsonUtil.convertValue(dataNode, Map.class);
		} else if (dataNode.isArray()) {
			return JsonUtil.convertValue(dataNode, List.class);
		}

		throw CodefException.from(CodefError.PARSE_ERROR);
	}

	private static Object parseExtraInfo(JsonNode jsonNode) {
		Map<String, Object> extraInfo = JsonUtil.toMap(jsonNode);

		if (extraInfo != null) {
			extraInfo.remove(RESULT.getValue());
			extraInfo.remove(DATA.getValue());
		}

		return extraInfo;
	}
}
