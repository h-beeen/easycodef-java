package io.codef.api.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Handler Layer] ResponseHandler Test")
public class ResponseHandlerTest {

	@Nested
	@DisplayName("[isSuccessResponse] HTTP 응답 객체 변환처리가 정상이면 성공")
	class isSuccessResponse {

		@Test
		@DisplayName("[Success] OAuth 토큰 응답 처리")
		void testProcessResponse_OAuthToken() throws UnsupportedEncodingException {
			String json = "{\"access_token\": \"test_token\", \"grant_type\": \"client_credentials\", \"scope\": \"read\"}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			assertNull(response.getResult());
			assertNotNull(response.getData());

			assertEquals("test_token", response.getData(Map.class).get("access_token"));
			assertEquals("client_credentials", response.getData(Map.class).get("grant_type"));
			assertEquals("read", response.getData(Map.class).get("scope"));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. Data가 객체인 경우")
		void testProcessResponse_ProductObject() throws UnsupportedEncodingException {
			String json = "{" +
				"\"result\": {\"code\": \"CF-00000\", \"message\": \"Success\"}," +
				"\"data\": {\"name\": \"hong\", \"age\": \"30\"}" +
				"}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			assertNotNull(response.getResult());
			assertEquals("CF-00000", response.getResult().getCode());
			assertEquals("Success", response.getResult().getMessage());

			assertEquals("hong", response.getData(Map.class).get("name"));
			assertEquals("30", response.getData(Map.class).get("age"));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. Data가 배열인 경우")
		void testProcessResponse_ProductArray() throws UnsupportedEncodingException {
			String json = "{" +
				"\"result\": {\"code\": \"CF-00000\"}," +
				"\"data\": [{\"id\": \"1\"}, {\"id\": \"2\"}]" +
				"}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			assertNotNull(response.getResult());

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> dataList = response.getData(List.class);
			assertEquals(2, dataList.size());
			assertEquals("1", dataList.get(0).get("id"));
		}

		@Test
		@DisplayName("[Success] 상품 API 응답 처리. ExtraInfo가 포함된 경우")
		void testProcessResponse_ExtraInfo() throws UnsupportedEncodingException {
			String json = "{" +
				"\"result\": {\"code\": \"CF-00000\"}," +
				"\"data\": {}," +
				"\"connectedId\": \"easycodef123\"" +
				"}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			EasyCodefResponse response = ResponseHandler.processResponse(encodedJson);

			assertNotNull(response.getExtraInfo());

			@SuppressWarnings("unchecked")
			Map<String, Object> extraInfo = (Map<String, Object>)response.getExtraInfo();
			assertEquals("easycodef123", extraInfo.get("connectedId"));
		}
	}

	@Nested
	@DisplayName("[Throw Exception] Exception Cases")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] result 필드가 없는 경우 PARSE_ERROR 예외처리")
		void testProcessResponse_MissingResult() throws UnsupportedEncodingException {
			String json = "{\"data\": {}}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class, () ->
				ResponseHandler.processResponse(encodedJson)
			);

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] result 필드가 없는 null인 경우 PARSE_ERROR 예외처리")
		void testProcessResponse_NullResult() throws UnsupportedEncodingException {
			String json = "{\"result\": null}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class, () ->
				ResponseHandler.processResponse(encodedJson)
			);

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] data 필드가 없는 경우 PARSE_ERROR 예외처리")
		void testProcessResponse_MissingData() throws UnsupportedEncodingException {
			String json = "{\"result\": {\"code\": \"CF-00000\"}}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class, () ->
				ResponseHandler.processResponse(encodedJson)
			);

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] data 필드가 null인 경우 PARSE_ERROR 예외처리")
		void testProcessResponse_NullData() throws UnsupportedEncodingException {
			String json = "{\"result\": {\"code\": \"CF-00000\"}, \"data\": null}";
			String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class, () ->
				ResponseHandler.processResponse(encodedJson)
			);

			assertEquals(CodefError.PARSE_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 잘못된 JSON 형식인 경우 JSON_PARSE_ERROR 예외처리")
		void testProcessResponse_InvalidJson() throws UnsupportedEncodingException {
			String invalidJson = "{invalid_json}";
			String encodedJson = URLEncoder.encode(invalidJson, StandardCharsets.UTF_8.name());

			CodefException exception = assertThrows(CodefException.class, () ->
				ResponseHandler.processResponse(encodedJson)
			);

			assertEquals(CodefError.JSON_PARSE_ERROR, exception.getCodefError());
		}
	}
}
