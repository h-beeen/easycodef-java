package io.codef.api.root;

import static io.codef.api.constant.OAuthConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.codef.api.EasyCodefToken;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.service.EasyCodefOAuthService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Root Layer] EasyCodefToken Test")
public class EasyCodefTokenTest {

	private static final String CLIENT_ID = "testClientId";
	private static final String CLIENT_SECRET = "testClientSecret";
	private static final String MOCK_ACCESS_TOKEN = "mockAccessToken123";
	private static final long MOCK_EXPIRES_IN_SECONDS = 3600; // 1 hour

	@Mock
	private EasyCodefOAuthService mockOAuthService;

	@BeforeEach
	void setUp() {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(ACCESS_TOKEN.getValue(), MOCK_ACCESS_TOKEN);
		dataMap.put(EXPIRES_IN.getValue(), MOCK_EXPIRES_IN_SECONDS);

		EasyCodefResponse mockResponse = EasyCodefResponse.from(dataMap);

		when(mockOAuthService.requestToken(any(String.class))).thenReturn(mockResponse);
	}

	@Nested
	@DisplayName("[isSuccessResponse] 성공적으로 Access Token 생성 후 반환하면 성공")
	class ValidAccessToken {

		@Test
		@DisplayName("[Constructor] OAuth 토큰 생성 및 Access Token 초기화")
		void constructor_initializesToken() throws Exception {
			EasyCodefToken easyCodefToken = createEasyCodefToken();

			verify(mockOAuthService, times(1)).requestToken(any(String.class));

			String accessToken = invokeGetAccessToken(easyCodefToken);
			assertEquals(MOCK_ACCESS_TOKEN, accessToken);

			String expectedOauthToken = invokeCreateOAuthToken(easyCodefToken);
			String actualOauthToken = getOauthTokenField(easyCodefToken);
			assertEquals(expectedOauthToken, actualOauthToken);

			LocalDateTime expiresAt = getExpiresAt(easyCodefToken);
			assertNotNull(expiresAt);
			assertTrue(expiresAt.isAfter(LocalDateTime.now().minusSeconds(1)));
		}

		@Test
		@DisplayName("[Success] 만료 임박 시 재발급 후 반환")
		void getValidAccessToken_expiringSoon() throws Exception {
			LocalDateTime nearFutureExpiry = LocalDateTime.now().plusHours(1);
			EasyCodefToken easyCodefToken = createEasyCodefTokenWithSpecificExpiry(nearFutureExpiry);

			String validAccessToken = easyCodefToken.getValidAccessToken();
			assertEquals("Bearer " + MOCK_ACCESS_TOKEN, validAccessToken);

			verify(mockOAuthService, times(2)).requestToken(any(String.class));

			String accessToken = invokeGetAccessToken(easyCodefToken);
			assertEquals(MOCK_ACCESS_TOKEN, accessToken);

			verify(mockOAuthService, times(3)).requestToken(any(String.class));
		}

		@Test
		@DisplayName("[Success] 만료된 경우 재발급 후 반환")
		void getValidAccessToken_expired() throws Exception {
			LocalDateTime pastExpiry = LocalDateTime.now().minusHours(1);
			EasyCodefToken easyCodefToken = createEasyCodefTokenWithSpecificExpiry(pastExpiry);

			String validAccessToken = easyCodefToken.getValidAccessToken();
			assertEquals("Bearer " + MOCK_ACCESS_TOKEN, validAccessToken);

			verify(mockOAuthService, times(2)).requestToken(any(String.class));

			String accessToken = invokeGetAccessToken(easyCodefToken);
			assertEquals(MOCK_ACCESS_TOKEN, accessToken);

			verify(mockOAuthService, times(3)).requestToken(any(String.class));
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 성공적으로 Token을 재생성하면 성공")
	class RefreshToken {

		@Test
		@DisplayName("[Success] OAuth API로부터 토큰을 성공적으로 발급")
		void refresh_success() throws Exception {
			EasyCodefToken easyCodefToken = createEasyCodefToken();

			verify(mockOAuthService, times(1)).requestToken(any(String.class));

			invokeRefreshToken(easyCodefToken);

			verify(mockOAuthService, times(2)).requestToken(any(String.class));

			String accessToken = getAccessTokenField(easyCodefToken);
			assertEquals(MOCK_ACCESS_TOKEN, accessToken);

			LocalDateTime expiresAt = getExpiresAt(easyCodefToken);
			assertNotNull(expiresAt);
			assertTrue(expiresAt.isAfter(LocalDateTime.now().minusSeconds(1)));
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 만료시간을 정상적으로 검증하면 성공")
	class isTokenExpiringSoon {

		@Test
		@DisplayName("[Success] 만료까지 24시간 미만으로 남은 경우 true")
		void isTokenExpiringSoon_soon() throws Exception {
			LocalDateTime nearFutureExpiry = LocalDateTime.now().plusHours(23);
			EasyCodefToken easyCodefToken = createEasyCodefTokenWithSpecificExpiry(LocalDateTime.now().plusHours(1));

			boolean result = invokeIsTokenExpiringSoon(easyCodefToken, nearFutureExpiry);

			assertTrue(result);
		}

		@Test
		@DisplayName("[Success] 이미 만료된 경우 true")
		void isTokenExpiringSoon_expired() throws Exception {
			LocalDateTime pastExpiry = LocalDateTime.now().minusHours(1);
			EasyCodefToken easyCodefToken = createEasyCodefTokenWithSpecificExpiry(LocalDateTime.now().plusHours(1));

			boolean result = invokeIsTokenExpiringSoon(easyCodefToken, pastExpiry);

			assertTrue(result);
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 성공적으로 OAuth 토큰을 생성하면 성공")
	class createOAuthToken {

		@Test
		@DisplayName("[Success] 클라이언트 아이디와 시크릿으로 OAuth 토큰 생성")
		void createOAuthToken_generatesCorrectToken() throws Exception {
			EasyCodefToken easyCodefToken = createEasyCodefToken();

			String expectedAuth = CLIENT_ID + ":" + CLIENT_SECRET;
			String expectedOauthToken = new String(Base64.encodeBase64(expectedAuth.getBytes()));

			String actualOauthToken = invokeCreateOAuthToken(easyCodefToken);

			assertEquals(expectedOauthToken, actualOauthToken);
		}
	}

	private EasyCodefToken createEasyCodefToken() throws Exception {
		Constructor<EasyCodefToken> constructor = EasyCodefToken.class.getDeclaredConstructor(
			String.class,
			String.class,
			EasyCodefOAuthService.class
		);
		constructor.setAccessible(true);

		return constructor.newInstance(CLIENT_ID, CLIENT_SECRET, mockOAuthService);
	}

	private EasyCodefToken createEasyCodefTokenWithSpecificExpiry(LocalDateTime expiry) throws Exception {
		EasyCodefToken easyCodefToken = createEasyCodefToken();
		setExpiresAt(easyCodefToken, expiry);
		return easyCodefToken;
	}

	private String invokeGetAccessToken(EasyCodefToken token) throws Exception {
		Method method = EasyCodefToken.class.getDeclaredMethod("getAccessToken");
		method.setAccessible(true);
		return (String) method.invoke(token);
	}

	private String invokeCreateOAuthToken(EasyCodefToken token) throws Exception {
		Method method = EasyCodefToken.class.getDeclaredMethod(
			"createOAuthToken",
			String.class,
			String.class
		);
		method.setAccessible(true);

		return (String) method.invoke(token, EasyCodefTokenTest.CLIENT_ID, EasyCodefTokenTest.CLIENT_SECRET);
	}

	private void invokeRefreshToken(EasyCodefToken token) throws Exception {
		Method method = EasyCodefToken.class.getDeclaredMethod("refreshToken");
		method.setAccessible(true);
		method.invoke(token);
	}

	private boolean invokeIsTokenExpiringSoon(EasyCodefToken token, LocalDateTime expiry) throws Exception {
		Method method = EasyCodefToken.class.getDeclaredMethod(
			"isTokenExpiringSoon",
			LocalDateTime.class
		);
		method.setAccessible(true);

		return (Boolean) method.invoke(token, expiry);
	}

	private LocalDateTime getExpiresAt(EasyCodefToken token) throws Exception {
		Field field = EasyCodefToken.class.getDeclaredField("expiresAt");
		field.setAccessible(true);

		return (LocalDateTime) field.get(token);
	}

	private void setExpiresAt(EasyCodefToken token, LocalDateTime expiry) throws Exception {
		Field field = EasyCodefToken.class.getDeclaredField("expiresAt");
		field.setAccessible(true);
		field.set(token, expiry);
	}

	private String getOauthTokenField(EasyCodefToken token) throws Exception {
		Field field = EasyCodefToken.class.getDeclaredField("oauthToken");
		field.setAccessible(true);

		return (String) field.get(token);
	}

	private String getAccessTokenField(EasyCodefToken token) throws Exception {
		Field field = EasyCodefToken.class.getDeclaredField("accessToken");
		field.setAccessible(true);

		return (String) field.get(token);
	}
}
