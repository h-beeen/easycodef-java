package io.codef.api.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[HTTP Layer] HttpRequestBuilder Test")
public class HttpRequestBuilderTest {

	@Nested
	@DisplayName("[Builder] 빌더 패턴 생성자 테스트")
	class BuilderTest {

		@Test
		@DisplayName("[Success] HttpRequestBuilder 기본 빌더 패턴 및 값 설정 테스트")
		void testBuilderPattern() {
			String url = "https://api.codef.io/v1/test";
			String body = "{\"data\":\"test\"}";
			String headerKey = "Content-Type";
			String headerValue = "application/json";

			CodefHttpRequest request = HttpRequestBuilder.builder()
				.url(url)
				.header(headerKey, headerValue)
				.body(body)
				.build();

			assertNotNull(request);
			assertEquals(url, request.getUrl());
			assertEquals(body, request.getBody());

			assertNotNull(request.getHeaders());
			assertEquals(headerValue, request.getHeaders().get(headerKey));
		}

		@Test
		@DisplayName("[Success] HttpRequestBuilder 다중 헤더 추가 테스트")
		void testMultipleHeaders() {
			String key1 = "Content-Type";
			String val1 = "application/json";
			String key2 = "Authorization";
			String val2 = "Bearer token";

			CodefHttpRequest request = HttpRequestBuilder.builder()
				.url("http://example.com")
				.header(key1, val1)
				.header(key2, val2)
				.build();

			assertEquals(2, request.getHeaders().size());
			assertEquals(val1, request.getHeaders().get(key1));
			assertEquals(val2, request.getHeaders().get(key2));
		}

		@Test
		@DisplayName("[Success] HttpRequestBuilder 값 설정 없이 build() 호출 시 상태 확인")
		void testBuildWithNulls() {
			CodefHttpRequest request = HttpRequestBuilder.builder().build();

			assertNotNull(request);
			assertNull(request.getUrl());
			assertNull(request.getBody());

			assertNotNull(request.getHeaders());
			assertTrue(request.getHeaders().isEmpty());
		}
	}
}
