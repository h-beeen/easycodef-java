package io.codef.api.root;

import static io.codef.api.constant.CodefConstant.*;
import static io.codef.api.error.CodefError.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
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

import io.codef.api.EasyCodefClient;
import io.codef.api.EasyCodefDispatcher;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Root Layer] EasyCodefClient Test")
public class EasyCodefClientTest {

	@Mock
	private EasyCodefDispatcher dispatcher;

	private final String publicKey = "test-public-key";

	private EasyCodefClient client;

	@BeforeEach
	void setUp() throws Exception {
		Constructor<EasyCodefClient> constructor = EasyCodefClient.class.getDeclaredConstructor(
			EasyCodefDispatcher.class,
			String.class);
		constructor.setAccessible(true);

		client = createEasyCodefClient();
	}

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 요청을 처리하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 유효한 요청시 dispatcher 호출")
		void requestProduct_success() throws Exception {
			Map<String, Object> params = new HashMap<>();
			params.put("param1", "value1");

			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl("/v1/kr/bank/p/account/list")
				.parameterMap(params)
				.build();

			EasyCodefResponse expectedResponse = EasyCodefResponse.from(new HashMap<>());

			Method dispatchMethod = EasyCodefDispatcher.class.getDeclaredMethod(
				"dispatchRequest",
				EasyCodefRequest.class);
			dispatchMethod.setAccessible(true);
			when(dispatchMethod.invoke(dispatcher, request)).thenReturn(expectedResponse);

			EasyCodefResponse actualResponse = client.requestProduct(request);

			assertEquals(expectedResponse, actualResponse);

			dispatchMethod.invoke(verify(dispatcher), request);
		}

		@Test
		@DisplayName("[Success] Two-Way 정보가 포함된 요청시 dispatcher 호출")
		void requestCertification_success() throws Exception {
			Map<String, Object> twoWayInfo = new HashMap<>();
			twoWayInfo.put("jobIndex", 1);
			twoWayInfo.put("threadIndex", 1);
			twoWayInfo.put("jti", "test-jti");
			twoWayInfo.put("twoWayTimestamp", 123456789L);

			Map<String, Object> params = new HashMap<>();
			params.put(IS_2WAY.getValue(), true);
			params.put(INFO_KEY.getValue(), twoWayInfo);

			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl("/v1/kr/bank/p/account/list")
				.parameterMap(params)
				.build();

			EasyCodefResponse expectedResponse = EasyCodefResponse.from(new HashMap<>());

			Method dispatchMethod = EasyCodefDispatcher.class.getDeclaredMethod("dispatchRequest",
				EasyCodefRequest.class);
			dispatchMethod.setAccessible(true);
			when(dispatchMethod.invoke(dispatcher, request)).thenReturn(expectedResponse);

			EasyCodefResponse actualResponse = client.requestCertification(request);

			assertEquals(expectedResponse, actualResponse);
			dispatchMethod.invoke(verify(dispatcher), request);
		}

		@Test
		@DisplayName("[Success] 퍼블릭 키 반환 확인")
		void getPublicKey_success() {
			assertEquals(publicKey, client.getPublicKey());
		}
	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] request가 null이면 EMPTY_EASYCODEF_REQUEST 예외처리")
		void requestProduct_null() {
			CodefException exception = assertThrows(CodefException.class, () -> client.requestProduct(null));

			assertEquals(EMPTY_EASYCODEF_REQUEST, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] Two-Way 키워드(is2Way)가 포함되면 INVALID_2WAY_KEYWORD 예외처리")
		void requestProduct_containsTwoWayKeyword() {
			Map<String, Object> params = new HashMap<>();
			params.put(IS_2WAY.getValue(), true);
			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl("/v1/kr/bank/p/account/list")
				.parameterMap(params)
				.build();

			CodefException exception = assertThrows(CodefException.class, () -> client.requestProduct(request));

			assertEquals(INVALID_2WAY_KEYWORD, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] Two-Way 정보가 누락되면 INVALID_2WAY_INFO 예외처리")
		void requestCertification_missingTwoWayInfo() {
			Map<String, Object> params = new HashMap<>();
			params.put("param1", "value1");

			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl("/v1/kr/bank/p/account/list")
				.parameterMap(params)
				.build();

			CodefException exception = assertThrows(CodefException.class, () -> client.requestCertification(request));

			assertEquals(INVALID_2WAY_INFO, exception.getCodefError());
		}
	}

	private EasyCodefClient createEasyCodefClient() throws Exception {
		Constructor<EasyCodefClient> constructor = EasyCodefClient.class.getDeclaredConstructor(
			EasyCodefDispatcher.class,
			String.class);
		constructor.setAccessible(true);

		return constructor.newInstance(dispatcher, publicKey);
	}
}
