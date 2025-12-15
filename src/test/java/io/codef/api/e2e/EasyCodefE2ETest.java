package io.codef.api.e2e;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.fixture.ClientInfoFixture;
import io.codef.api.fixture.ParameterMapFixture;
import io.codef.api.fixture.ProductURL;

@DisplayName("[E2E Layer] EasyCodef (deprecated) E2E Test")
public class EasyCodefE2ETest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static EasyCodef easyCodef;

	@BeforeAll
	static void setUp() {
		easyCodef = ClientInfoFixture.demoFromEnv();
	}

	@Nested
	@DisplayName("[isSuccessResponse] 상품 API 호출이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 침수차량조회 상품 API 호출 시 정상 응답")
		void requestProduct_success() throws JsonProcessingException {
			String productUrl = ProductURL.FLOODED_VEHICLE.getUrl();
			Map<String, Object> parameterMap = ParameterMapFixture.floodedVehicleRequestParameterMap();

			String response = easyCodef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
			JsonNode root = mapper.readTree(response);

			String transactionId = root.path("result").path("transactionId").asText();

			assertAll(
				() -> assertNotNull(response),
				() -> assertNotNull(transactionId));
		}

		@Test
		@DisplayName("[Success] 존재하지 않는 상품 URL로 API 호출 시 CF-00003 정상 응답")
		void requestProduct_notExistURL() throws JsonProcessingException {
			String productUrl = ProductURL.FLOODED_VEHICLE_WRONG.getUrl();
			Map<String, Object> parameterMap = ParameterMapFixture.floodedVehicleRequestParameterMap();

			String response = easyCodef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
			JsonNode root = mapper.readTree(response);

			boolean hasTransactionId = root.path("result").has("transactionId");

			assertAll(
				() -> assertNotNull(response),
				() -> assertFalse(hasTransactionId));
		}
	}

	@Nested
	@DisplayName("[isSuccessToken] 토큰이 정상적으로 발급되면 성공")
	class TokenResponseCases {

		@Test
		@DisplayName("[Success] 토큰 발급 확인")
		void requestToken_success() {
			String firstIssuedToken = easyCodef.requestToken(EasyCodefServiceType.DEMO);
			String reusedToken = easyCodef.requestToken(EasyCodefServiceType.DEMO);

			assertValidJwt(firstIssuedToken);
			assertEquals(firstIssuedToken, reusedToken);
		}

		@Test
		@DisplayName("[Success] 새로운 토큰 발급 확인")
		void requestNewToken_success() {
			String firstIssuedToken = easyCodef.requestToken(EasyCodefServiceType.DEMO);
			String newIssuedToken = easyCodef.requestNewToken(EasyCodefServiceType.DEMO);

			assertValidJwt(newIssuedToken);
			assertNotEquals(firstIssuedToken, newIssuedToken);
		}

		private void assertValidJwt(String token) {
			String[] parts = token.split("\\.");

			assertAll(
				() -> assertEquals(3, parts.length),
				() -> assertDoesNotThrow(() -> Base64.getUrlDecoder().decode(parts[0])),
				() -> assertDoesNotThrow(() -> Base64.getUrlDecoder().decode(parts[1])),
				() -> assertFalse(parts[2].isEmpty()));
		}
	}
}
