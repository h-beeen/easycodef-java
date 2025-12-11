package io.codef.api.e2e.fixture;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TwoWayFixture {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private TwoWayFixture() {}

	public static String twoWayRequiredResponseJson() {
		Map<String, Object> twoWayInfo = new HashMap<>();
		twoWayInfo.put("jobIndex", 1);
		twoWayInfo.put("jti", "mock_jti_123");

		Map<String, Object> data = new HashMap<>();
		data.put("twoWayInfo", twoWayInfo);

		return toJson(createResponseMap("CF-03002", "추가인증 필요", data));
	}

	public static String successResponseJson() {
		Map<String, Object> data = new HashMap<>();
		data.put("resAuthName", "홍길동");

		return toJson(createResponseMap("CF-00000", "성공", data));
	}

	public static HashMap<String, Object> firstRequestParams() {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("organization", "0000");
		return paramMap;
	}

	private static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON 변환 실패", e);
		}
	}

	private static Map<String, Object> createResponseMap(String code, String message, Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		Map<String, Object> result = new HashMap<>();
		result.put("code", code);
		result.put("message", message);

		response.put("result", result);
		response.put("data", data);

		return response;
	}
}
