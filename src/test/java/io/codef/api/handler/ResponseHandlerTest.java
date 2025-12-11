package io.codef.api.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Handler Layer] ResponseHandler Test")
public class ResponseHandlerTest {

	@Nested
	@DisplayName("[isSuccessResponse] HTTP 응답 객체 변환처리가 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] OAuth 토큰 응답 처리")
		void processResponse_OAuthToken() throws Exception {
			Map<String, Object> data = new HashMap<>();
			data.put("access_token", "test_token");
			data.put("grant_type", "client_credentials");
			data.put("scope", "read");

			String encodedJson = encodeJson(data);

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			Map<?, ?> responseData = response.getData(Map.class);

			assertAll(
				() -> assertNull(response.getResult()),
				() -> assertNotNull(response.getData()),
				() -> assertEquals("test_token", responseData.get("access_token")),
				() -> assertEquals("client_credentials", responseData.get("grant_type")),
				() -> assertEquals("read", responseData.get("scope")));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. Data가 객체인 경우")
		void processResponse_dataObject() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");
			result.put("message", "Success");

			Map<String, Object> data = new HashMap<>();
			data.put("name", "hong");
			data.put("age", "30");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);

			String encodedJson = encodeJson(root);

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			Map<?, ?> responseData = response.getData(Map.class);

			assertAll(
				() -> assertNotNull(response.getResult()),
				() -> assertEquals("CF-00000", response.getResult().getCode()),
				() -> assertEquals("Success", response.getResult().getMessage()),
				() -> assertEquals("hong", responseData.get("name")),
				() -> assertEquals("30", responseData.get("age")));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. Data가 배열인 경우")
		void processResponse_dataArray() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");

			Map<String, Object> item1 = new HashMap<>();
			item1.put("id", "1");
			Map<String, Object> item2 = new HashMap<>();
			item2.put("id", "2");

			List<Map<String, Object>> data = new ArrayList<>();
			data.add(item1);
			data.add(item2);

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);

			String encodedJson = encodeJson(root);

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			@SuppressWarnings("unchecked") List<Map<String, Object>> dataList = response.getData(List.class);

			assertAll(
				() -> assertNotNull(response.getResult()),
				() -> assertEquals(2, dataList.size()),
				() -> assertEquals("1", dataList.get(0).get("id")));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. ExtraInfo가 포함된 경우")
		void processResponse_extraInfo() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");

			Map<String, Object> data = new HashMap<>();

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);
			root.put("connectedId", "easycodef123");

			String encodedJson = encodeJson(root);

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			@SuppressWarnings("unchecked") Map<String, Object> extraInfo = (Map<String, Object>)response.getExtraInfo();

			assertAll(
				() -> assertNotNull(response.getExtraInfo()),
				() -> assertEquals("easycodef123", extraInfo.get("connectedId")));
		}
	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] result 필드가 없는 경우 PARSE_ERROR 예외처리")
		void processResponse_missingResult() throws Exception {
			Map<String, Object> root = new HashMap<>();
			root.put("data", new HashMap<String, Object>());

			String encodedJson = encodeJson(root);

			CodefException exception = assertThrows(CodefException.class,
				() -> ResponseHandler.processResponse(encodedJson));

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] result 필드가 null인 경우 PARSE_ERROR 예외처리")
		void processResponse_nullResult() throws Exception {
			Map<String, Object> root = new HashMap<>();
			root.put("result", null);

			String encodedJson = encodeJson(root);

			CodefException exception = assertThrows(CodefException.class,
				() -> ResponseHandler.processResponse(encodedJson));

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] data 필드가 없는 경우 PARSE_ERROR 예외처리")
		void processResponse_missingData() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);

			String encodedJson = encodeJson(root);

			CodefException exception = assertThrows(CodefException.class,
				() -> ResponseHandler.processResponse(encodedJson));

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] data 필드가 null인 경우 PARSE_ERROR 예외처리")
		void processResponse_nullData() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", null);

			String encodedJson = encodeJson(root);

			CodefException exception = assertThrows(CodefException.class,
				() -> ResponseHandler.processResponse(encodedJson));

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 잘못된 JSON 형식인 경우 JSON_PARSE_ERROR 예외처리")
		void processResponse_invalidJson() throws UnsupportedEncodingException {
			String invalidJson = "{invalid_json}";
			String encodedJson = URLEncoder.encode(invalidJson, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class,
				() -> ResponseHandler.processResponse(encodedJson));

			assertEquals(CodefError.JSON_PARSE_ERROR, exception.getCodefError());
		}
	}

	private String encodeJson(Object value) throws Exception {
		String json = new ObjectMapper().writeValueAsString(value);
		return URLEncoder.encode(json, StandardCharsets.UTF_8.name());
	}
}
