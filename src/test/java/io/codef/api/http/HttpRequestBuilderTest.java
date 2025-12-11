package io.codef.api.http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("[HTTP Layer] HttpRequestBuilder Test")
public class HttpRequestBuilderTest {

	@Nested
	@DisplayName("[isSuccessResponse] 생성이 정상적으로 완료되면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] HttpRequestBuilder 기본 빌더 패턴 및 값 설정 테스트")
		void build_success() throws JsonProcessingException {
			String url = "https://api.codef.io/v1/test";

			Map<String, Object> bodyMap = new HashMap<>();
			bodyMap.put("data", "test");
			String body = new ObjectMapper().writeValueAsString(bodyMap);

			String headerKey = "Content-Type";
			String headerValue = "application/json";

			CodefHttpRequest request = HttpRequestBuilder.builder()
				.url(url)
				.header(headerKey, headerValue)
				.body(body)
				.build();

			assertAll(
				() -> assertEquals(url, request.getUrl()),
				() -> assertEquals(body, request.getBody()),
				() -> assertNotNull(request.getHeaders()),
				() -> assertEquals(headerValue, request.getHeaders().get(headerKey)));
		}

		@Test
		@DisplayName("[Success] HttpRequestBuilder 다중 헤더 추가 테스트")
		void setHeaders_success() {
			String key1 = "Content-Type";
			String val1 = "application/json";
			String key2 = "Authorization";
			String val2 = "Bearer token";

			CodefHttpRequest request = HttpRequestBuilder.builder()
				.url("http://example.com")
				.header(key1, val1)
				.header(key2, val2)
				.build();

			Map<String, String> headers = request.getHeaders();

			assertAll(
				() -> assertEquals(2, headers.size()),
				() -> assertEquals(val1, headers.get(key1)),
				() -> assertEquals(val2, headers.get(key2)));
		}

		@Test
		@DisplayName("[Success] HttpRequestBuilder 값 설정 없이 build() 호출 시 상태 확인")
		void build_success_without_HttpRequestBuilder() {
			CodefHttpRequest request = HttpRequestBuilder.builder().build();

			Map<String, String> headers = request.getHeaders();

			assertAll(
				() -> assertNull(request.getUrl()),
				() -> assertNull(request.getBody()),
				() -> assertNotNull(headers),
				() -> assertTrue(headers.isEmpty()));
		}
	}
}
