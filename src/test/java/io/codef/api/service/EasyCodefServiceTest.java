package io.codef.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service Layer] EasyCodefService Test")
public class EasyCodefServiceTest {

	@Mock
	private CodefHttpClient httpClient;

	private EasyCodefService easyCodefService;

	@BeforeEach
	void setUp() {
		easyCodefService = new EasyCodefApiService(httpClient);
	}

	@Nested
	@DisplayName("[isSuccessResponse] 요청이 정상적으로 호출되고 응답이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] EasyCodefService 생성자 테스트")
		void constructor_success() {
			assertNotNull(easyCodefService);
		}

		@Test
		@DisplayName("[Success] sendRequest 상품 요청 정상 응답")
		void sendRequest_success() throws Exception {
			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");
			result.put("message", "Success");
			result.put("extraMessage", "");

			Map<String, Object> data = new HashMap<>();
			data.put("name", "test");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);

			String rawJson = new ObjectMapper().writeValueAsString(root);
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			CodefHttpRequest request = new CodefHttpRequest("https://example.com", new HashMap<>(), "{}");

			EasyCodefResponse response = easyCodefService.sendRequest(request);

			Map<?, ?> responseData = response.getData(Map.class);

			assertAll(
				() -> assertNotNull(response),
				() -> assertNotNull(response.getResult()),
				() -> assertEquals("CF-00000", response.getResult().getCode()),
				() -> assertEquals("Success", response.getResult().getMessage()),
				() -> assertEquals("test", responseData.get("name")),
				() -> verify(httpClient, times(1)).execute(request));
		}

		@Test
		@DisplayName("[Success] sendRequest 토큰 요청 정상 응답")
		void sendRequest_TokenResponse() throws Exception {
			Map<String, Object> tokenMap = new HashMap<>();
			tokenMap.put("access_token", "Bearer token123");

			String rawJson = new ObjectMapper().writeValueAsString(tokenMap);
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			CodefHttpRequest request = new CodefHttpRequest("https://example.com", new HashMap<>(), "{}");

			EasyCodefResponse response = easyCodefService.sendRequest(request);

			Map<?, ?> responseData = response.getData(Map.class);

			assertAll(
				() -> assertNotNull(response),
				() -> assertNull(response.getResult()),
				() -> assertEquals("Bearer token123", responseData.get("access_token")),
				() -> verify(httpClient, times(1)).execute(request));
		}
	}
}
