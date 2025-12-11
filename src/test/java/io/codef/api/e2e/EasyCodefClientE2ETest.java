package io.codef.api.e2e;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.EasyCodefClient;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.fixture.ClientInfoFixture;
import io.codef.api.fixture.ParameterMapFixture;
import io.codef.api.fixture.ProductURL;
import io.codef.api.fixture.TokenFixture;

@DisplayName("[E2E Layer] EasyCodefClient E2E Test")
public class EasyCodefClientE2ETest {

	private static EasyCodefClient easyCodefClient;

	@BeforeAll
	static void setUp() {
		easyCodefClient = ClientInfoFixture.demoClientFromEnv();
	}

	@Nested
	@DisplayName("[isSuccessResponse] 상품 API 호출이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 침수차량조회 상품 API 호출 시 정상 응답")
		void requestProduct_success() {
			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl(ProductURL.FLOODED_VEHICLE.getUrl())
				.parameterMap(ParameterMapFixture.floodedVehicleRequestParameterMap())
				.build();

			EasyCodefResponse response = easyCodefClient.requestProduct(request);

			String transactionId = response.getResult().getTransactionId();

			assertAll(
				() -> assertNotNull(response),
				() -> assertNotNull(transactionId),
				() -> assertNotNull(response.getData()));
		}

		@Test
		@DisplayName("[Success] 존재하지 않는 상품 URL로 API 호출 시 CF-00003 정상 응답")
		void requestProduct_notExistURL() {
			EasyCodefRequest request = EasyCodefRequestBuilder.builder()
				.productUrl(ProductURL.FLOODED_VEHICLE_WRONG.getUrl())
				.parameterMap(ParameterMapFixture.floodedVehicleRequestParameterMap())
				.build();

			EasyCodefResponse response = easyCodefClient.requestProduct(request);

			String transactionId = response.getResult().getTransactionId();

			assertAll(
				() -> assertNotNull(response),
				() -> assertNull(transactionId));
		}

		@Test
		@DisplayName("[Success] 초기에 설정한 publicKey 정상 반환")
		void getPublicKey_success() {
			String envPublicKey = System.getenv("PUBLIC_KEY");
			String getPublicKey = easyCodefClient.getPublicKey();

			assertEquals(envPublicKey, getPublicKey);
		}
	}

	@Nested
	@DisplayName("[isSuccessToken] 토큰이 정상적으로 발급되면 성공")
	class TokenResponseCases {

		@Test
		@DisplayName("[Success] 토큰 만료 시 자동 갱신 후 API 호출 성공")
		void getAccessToken_refresh() throws Exception {
			String productUrl = ProductURL.FLOODED_VEHICLE.getUrl();
			Map<String, Object> parameterMap = ParameterMapFixture.floodedVehicleRequestParameterMap();

			EasyCodefRequest firstRequest = EasyCodefRequestBuilder.builder()
				.productUrl(productUrl)
				.parameterMap(parameterMap)
				.build();

			EasyCodefResponse firstResponse = easyCodefClient.requestProduct(firstRequest);

			Object tokenObj = TokenFixture.getTokenObjectInClient(easyCodefClient);
			String oldAccessToken = TokenFixture.getAccessToken(tokenObj);
			TokenFixture.forceExpireToken(tokenObj);

			EasyCodefRequest secondRequest = EasyCodefRequestBuilder.builder()
				.productUrl(productUrl)
				.parameterMap(parameterMap)
				.build();

			EasyCodefResponse secondResponse = easyCodefClient.requestProduct(secondRequest);

			String newAccessToken = TokenFixture.getAccessToken(tokenObj);

			assertAll(
				() -> assertNotNull(firstResponse),
				() -> assertNotNull(secondResponse),
				() -> assertNotEquals(oldAccessToken, newAccessToken));
		}
	}
}
