package io.codef.api.service;

import static io.codef.api.constant.CodefHost.*;
import static io.codef.api.constant.CodefPath.*;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service Layer] EasyCodefOAuthService Test")
public class EasyCodefOAuthServiceTest {

	@Mock
	private CodefHttpClient httpClient;

	private EasyCodefOAuthService easyCodefOAuthService;

	@BeforeEach
	void setUp() {
		easyCodefOAuthService = new EasyCodefOAuthService(httpClient);
	}

	@Nested
	@DisplayName("[isSuccessResponse] 요청부가 정상적으로 생성되고 응답이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] EasyCodefOAuthService 생성자 테스트")
		void constructor_success() {
			assertNotNull(easyCodefOAuthService);
		}

		@Test
		@DisplayName("[Success] 토큰 발급 성공")
		void requestToken_Success() throws Exception {
			String basicToken = "Basic aHR0cHN...";

			Map<String, Object> tokenBody = new HashMap<>();
			tokenBody.put("access_token", "mockAccessToken");
			tokenBody.put("expires_in", 3600);

			String rawJson = new ObjectMapper().writeValueAsString(tokenBody);
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			EasyCodefResponse response = easyCodefOAuthService.requestToken(basicToken);

			Map<?, ?> data = response.getData(Map.class);

			assertAll(
				() -> assertNotNull(response),
				() -> assertNull(response.getResult()),
				() -> assertEquals("mockAccessToken", data.get("access_token")),
				() -> assertEquals(3600, data.get("expires_in")));
		}

		@Test
		@DisplayName("[Success] 요청 객체 생성 및 전달 확인")
		void requestToken_VerifyRequest() throws Exception {
			String basicToken = "Basic aHR0cHN...";

			Map<String, Object> tokenMap = new HashMap<>();
			tokenMap.put("access_token", "mockAccessToken");
			tokenMap.put("expires_in", 3600);

			String rawJson = new ObjectMapper().writeValueAsString(tokenMap);
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			EasyCodefResponse response = easyCodefOAuthService.requestToken(basicToken);

			ArgumentCaptor<CodefHttpRequest> requestCaptor = ArgumentCaptor.forClass(CodefHttpRequest.class);
			verify(httpClient, times(1)).execute(requestCaptor.capture());

			CodefHttpRequest capturedRequest = requestCaptor.getValue();

			Map<?, ?> data = response.getData(Map.class);

			assertAll(
				() -> assertNotNull(capturedRequest.getHeaders()),
				() -> assertEquals(OAUTH_DOMAIN + GET_TOKEN, capturedRequest.getUrl()),
				() -> assertEquals(basicToken, capturedRequest.getHeaders().get("Authorization")),
				() -> assertEquals("mockAccessToken", data.get("access_token")),
				() -> assertEquals(3600, data.get("expires_in")));
		}

	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] CodefException 발생 시 예외 전파")
		void testRequestToken_ThrowsCodefException() {
			String basicToken = "Basic aHR0cHN...";
			when(httpClient.execute(any(CodefHttpRequest.class))).thenThrow(CodefException.from(CodefError.IO_ERROR));

			CodefException exception = assertThrows(CodefException.class,
				() -> easyCodefOAuthService.requestToken(basicToken));

			assertAll(
				() -> assertEquals(CodefError.IO_ERROR, exception.getCodefError()),
				() -> verify(httpClient, times(1)).execute(any(CodefHttpRequest.class)));
		}
	}
}
