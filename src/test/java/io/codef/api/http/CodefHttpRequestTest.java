package io.codef.api.http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("[HTTP Layer] CodefHttpRequest Test")
public class CodefHttpRequestTest {

	@Test
	@DisplayName("[Success] CodefHttpRequest 생성자 및 Getter 테스트")
	void constructor_success() throws JsonProcessingException {
		String url = "https://api.codef.io/v1/test";

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer token");

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("param", "value");
		String body = new ObjectMapper().writeValueAsString(bodyMap);

		CodefHttpRequest request = new CodefHttpRequest(url, headers, body);

		assertAll(
			() -> assertNotNull(request),
			() -> assertEquals(url, request.getUrl()),
			() -> assertSame(headers, request.getHeaders()),
			() -> assertEquals(body, request.getBody()));
	}
}
