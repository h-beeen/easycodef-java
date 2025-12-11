package io.codef.api.service;

import static io.codef.api.constant.CodefConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service Layer] EasyCodefApiService Test")
public class EasyCodefApiServiceTest {

	@Mock
	private CodefHttpClient httpClient;

	private EasyCodefApiService easyCodefApiService;

	@BeforeEach
	void setUp() {
		easyCodefApiService = new EasyCodefApiService(httpClient);
	}

	@Nested
	@DisplayName("[isSuccessResponse] 요청부가 정상적으로 생성되고 응답이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] EasyCodefApiService 생성자 테스트")
		void constructor_success() {
			assertNotNull(easyCodefApiService);
		}

		@Test
		@DisplayName("[Success] 상품 요청 성공")
		void requestProduct_success() throws JsonProcessingException {
			String TEST_URL_PATH = "/v1/test/product";
			String TEST_BEARER_TOKEN = "Bearer abcdef12345";

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("param1", "value1");
			String TEST_JSON_BODY = new ObjectMapper().writeValueAsString(requestBody);

			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");
			result.put("message", "성공");

			Map<String, Object> data = new HashMap<>();
			data.put("key", "value");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);

			String successResponseJson = new ObjectMapper().writeValueAsString(root);

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(successResponseJson);

			EasyCodefResponse response = easyCodefApiService.requestProduct(TEST_URL_PATH, TEST_BEARER_TOKEN,
				TEST_JSON_BODY);

			assertAll(
				() -> assertNotNull(response),
				() -> assertEquals("CF-00000", response.getResult().getCode()),
				() -> assertEquals("성공", response.getResult().getMessage()),
				() -> assertNotNull(response.getData()));
		}

		@Test
		@DisplayName("[Success] 요청 객체 생성 및 전달 확인")
		void requestProduct_VerifyRequest() throws JsonProcessingException {
			String TEST_URL_PATH = "/v1/test/product";
			String TEST_BEARER_TOKEN = "Bearer abcdef12345";

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("param1", "value1");
			String TEST_JSON_BODY = new ObjectMapper().writeValueAsString(requestBody);

			Map<String, Object> result = new HashMap<>();
			result.put("code", "CF-00000");
			result.put("message", "성공");

			Map<String, Object> data = new HashMap<>();
			data.put("key", "value");

			Map<String, Object> root = new HashMap<>();
			root.put("result", result);
			root.put("data", data);

			String successResponseJson = new ObjectMapper().writeValueAsString(root);

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(successResponseJson);

			easyCodefApiService.requestProduct(TEST_URL_PATH, TEST_BEARER_TOKEN, TEST_JSON_BODY);

			ArgumentCaptor<CodefHttpRequest> requestCaptor = ArgumentCaptor.forClass(CodefHttpRequest.class);
			verify(httpClient, times(1)).execute(requestCaptor.capture());

			CodefHttpRequest capturedRequest = requestCaptor.getValue();

			assertAll(
				() -> assertEquals(TEST_URL_PATH, capturedRequest.getUrl()),
				() -> assertEquals(TEST_BEARER_TOKEN,
					capturedRequest.getHeaders().get("Authorization")),
				() -> assertEquals(APPLICATION_JSON.getValue(),
					capturedRequest.getHeaders().get("Content-Type")),
				() -> assertEquals(TEST_JSON_BODY, capturedRequest.getBody()));
		}

	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] CodefException 발생 시 예외 전파")
		void requestToken_ThrowsCodefException() throws JsonProcessingException {
			String TEST_URL_PATH = "/v1/test/product";
			String TEST_BEARER_TOKEN = "Bearer abcdef12345";

			Map<String, Object> bodyMap = new HashMap<>();
			bodyMap.put("param1", "value1");
			String TEST_JSON_BODY = new ObjectMapper().writeValueAsString(bodyMap);

			when(httpClient.execute(any(CodefHttpRequest.class))).thenThrow(CodefException.from(CodefError.IO_ERROR));

			CodefException exception = assertThrows(CodefException.class, () -> easyCodefApiService.requestProduct(
				TEST_URL_PATH,
				TEST_BEARER_TOKEN,
				TEST_JSON_BODY));

			assertAll(
				() -> assertEquals(CodefError.IO_ERROR, exception.getCodefError()),
				() -> verify(httpClient, times(1)).execute(any(CodefHttpRequest.class)));
		}
	}
}
