package io.codef.api.service;

import static io.codef.api.constant.CodefHost.*;
import static io.codef.api.constant.CodefPath.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	@DisplayName("[constructor] 생성자 테스트")
	class NewConstructor {

		@Test
		@DisplayName("[Success] EasyCodefOAuthService 생성자 테스트")
		void testConstructor() {
			assertNotNull(easyCodefOAuthService);
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 요청부가 정상적으로 생성되고 응답이 정상이면 성공")
	class MethodCases {

		@Test
		@DisplayName("[Success] 토큰 발급 성공")
		void testRequestToken_Success() throws Exception {
			String basicToken = "Basic aHR0cHN...";
			String rawJson = "{\"access_token\":\"mockAccessToken\",\"expires_in\":3600}";
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			EasyCodefResponse response = easyCodefOAuthService.requestToken(basicToken);

			assertNotNull(response);
			assertNull(response.getResult());

			assertEquals("mockAccessToken", response.getData(Map.class).get("access_token"));
			assertEquals(3600, response.getData(Map.class).get("expires_in"));
		}

		@Test
		@DisplayName("[Verification] 요청 객체 생성 및 전달 확인")
		void testRequestToken_VerifyRequest() throws Exception {
			String basicToken = "Basic aHR0cHN...";
			String rawJson = "{\"access_token\":\"mockAccessToken\",\"expires_in\":3600}";
			String encodedResponse = URLEncoder.encode(rawJson, StandardCharsets.UTF_8.name());

			when(httpClient.execute(any(CodefHttpRequest.class))).thenReturn(encodedResponse);

			EasyCodefResponse response = easyCodefOAuthService.requestToken(basicToken);

			ArgumentCaptor<CodefHttpRequest> requestCaptor = ArgumentCaptor.forClass(CodefHttpRequest.class);
			verify(httpClient, times(1)).execute(requestCaptor.capture());

			CodefHttpRequest capturedRequest = requestCaptor.getValue();

			assertNotNull(capturedRequest.getHeaders());
			assertEquals(OAUTH_DOMAIN + GET_TOKEN, capturedRequest.getUrl());
			assertEquals(basicToken, capturedRequest.getHeaders().get("Authorization"));
			assertEquals("mockAccessToken", response.getData(Map.class).get("access_token"));
		}

		@Test
		@DisplayName("[Exception] CodefException 발생 시 예외 전파")
		void testRequestToken_ThrowsCodefException() {
			String basicToken = "Basic aHR0cHN...";
			when(httpClient.execute(any(CodefHttpRequest.class))).thenThrow(CodefException.from(CodefError.IO_ERROR));

			CodefException exception = assertThrows(CodefException.class, () -> easyCodefOAuthService.requestToken(basicToken));

			assertEquals(CodefError.IO_ERROR, exception.getCodefError());
			verify(httpClient, times(1)).execute(any(CodefHttpRequest.class));
		}
	}
}
