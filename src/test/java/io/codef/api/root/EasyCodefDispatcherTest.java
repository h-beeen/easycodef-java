package io.codef.api.root;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

import io.codef.api.EasyCodefDispatcher;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefToken;
import io.codef.api.constant.CodefHost;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.service.EasyCodefApiService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Root Layer] EasyCodefDispatcher Test")
public class EasyCodefDispatcherTest {

	@Mock
	private EasyCodefToken mockToken;

	@Mock
	private EasyCodefApiService mockApiService;

	private EasyCodefDispatcher dispatcher;

	@BeforeEach
	void setUp() throws Exception {
		dispatcher = createEasyCodefDispatcher();
	}

	@Nested
	@DisplayName("[isSuccessResponse] 생성자 호출과 서비스 객체로 정상 전달하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] EasyCodefToken, EasyCodefServiceType, EasyCodefApiService 정상 초기화")
		void constructor_success() throws Exception {
			EasyCodefServiceType serviceType = EasyCodefServiceType.DEMO;

			EasyCodefDispatcher newDispatcher = createEasyCodefDispatcher();

			Field tokenField = EasyCodefDispatcher.class.getDeclaredField("token");
			tokenField.setAccessible(true);
			Field serviceTypeField = EasyCodefDispatcher.class.getDeclaredField("easyCodefServiceType");
			serviceTypeField.setAccessible(true);
			Field apiServiceField = EasyCodefDispatcher.class.getDeclaredField("apiService");
			apiServiceField.setAccessible(true);

			assertAll(
				() -> assertEquals(mockToken, tokenField.get(newDispatcher)),
				() -> assertEquals(serviceType, serviceTypeField.get(newDispatcher)),
				() -> assertEquals(mockApiService, apiServiceField.get(newDispatcher)));
		}

		@Test
		@DisplayName("[Success] API 요청을 URL, 토큰, 바디로 변환하여 서비스로 전달")
		void dispatchRequest_success() throws Exception {
			String productUrl = "/v1/kr/bank/account/list";
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("organization", "0004");
			paramMap.put("id", "testId");

			EasyCodefRequest request = EasyCodefRequestBuilder
				.builder()
				.productUrl(productUrl)
				.parameterMap(paramMap)
				.build();

			String expectedToken = "Bearer testAccessToken";
			when(mockToken.getValidAccessToken()).thenReturn(expectedToken);

			EasyCodefResponse expectedResponse = EasyCodefResponse.from(new HashMap<>());
			when(mockApiService.requestProduct(anyString(), anyString(), anyString()))
				.thenReturn(expectedResponse);

			Method method = EasyCodefDispatcher.class.getDeclaredMethod(
				"dispatchRequest",
				EasyCodefRequest.class);
			method.setAccessible(true);

			EasyCodefResponse actualResponse = (EasyCodefResponse)method.invoke(dispatcher, request);

			assertEquals(expectedResponse, actualResponse);

			verify(mockToken, times(1)).getValidAccessToken();

			String expectedUrl = CodefHost.DEMO_DOMAIN + productUrl;
			String expectedJsonBody = new ObjectMapper().writeValueAsString(paramMap);

			verify(mockApiService, times(1)).requestProduct(
				eq(expectedUrl),
				eq(expectedToken),
				eq(expectedJsonBody));
		}
	}

	@Test
	@DisplayName("[Success] getToken 호출 시 Access Token 반환")
	void getToken_success() throws Exception {
		String expectedToken = "test-access-token";

		Method getAccessTokenMethod = EasyCodefToken.class.getDeclaredMethod("getAccessToken");
		getAccessTokenMethod.setAccessible(true);

		when(getAccessTokenMethod.invoke(mockToken)).thenReturn(expectedToken);

		Method getTokenMethod = EasyCodefDispatcher.class.getDeclaredMethod("getToken");
		getTokenMethod.setAccessible(true);
		String actualToken = (String)getTokenMethod.invoke(dispatcher);

		assertEquals(expectedToken, actualToken);
		getAccessTokenMethod.invoke(verify(mockToken, times(1)));
	}

	@Test
	@DisplayName("[Success] getNewToken 호출 시 새로운 Access Token 반환")
	void getNewToken_success() throws Exception {
		String expectedNewToken = "new-test-access-token";

		Method getNewAccessTokenMethod = EasyCodefToken.class.getDeclaredMethod("getNewAccessToken");
		getNewAccessTokenMethod.setAccessible(true);
		when(getNewAccessTokenMethod.invoke(mockToken)).thenReturn(expectedNewToken);

		Method getNewTokenMethod = EasyCodefDispatcher.class.getDeclaredMethod("getNewToken");
		getNewTokenMethod.setAccessible(true);
		String actualNewToken = (String)getNewTokenMethod.invoke(dispatcher);

		assertEquals(expectedNewToken, actualNewToken);
		getNewAccessTokenMethod.invoke(verify(mockToken, times(1)));
	}

	private EasyCodefDispatcher createEasyCodefDispatcher() throws Exception {
		Constructor<EasyCodefDispatcher> constructor = EasyCodefDispatcher.class.getDeclaredConstructor(
			EasyCodefToken.class,
			EasyCodefServiceType.class,
			EasyCodefApiService.class);
		constructor.setAccessible(true);

		return constructor.newInstance(mockToken, EasyCodefServiceType.DEMO, mockApiService);
	}
}
