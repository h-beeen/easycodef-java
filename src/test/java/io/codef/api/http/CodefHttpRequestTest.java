package io.codef.api.http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[HTTP Layer] CodefHttpRequest Test")
public class CodefHttpRequestTest {

	@Test
	@DisplayName("[Success] CodefHttpRequest 생성자 및 Getter 테스트")
	void testConstructorInitialization() {
		String url = "https://api.codef.io/v1/test";
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer token");
		String body = "{\"param\":\"value\"}";

		CodefHttpRequest request = new CodefHttpRequest(url, headers, body);

		assertNotNull(request);
		assertEquals(url, request.getUrl());
		assertSame(headers, request.getHeaders());
		assertEquals(body, request.getBody());
	}
}
