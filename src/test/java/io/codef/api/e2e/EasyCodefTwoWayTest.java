package io.codef.api.e2e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefClient;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.fixture.ClientInfoFixture;
import io.codef.api.fixture.TwoWayFixture;

@DisplayName("[E2E Layer] EasyCodef TwoWay Test")
class EasyCodefTwoWayTest {

	private final ObjectMapper mapper = new ObjectMapper();

	private EasyCodef easyCodef;
	private EasyCodefClient mockClient;

	@BeforeEach
	void setUp() throws Exception {
		easyCodef = ClientInfoFixture.demoFromEnv();
		mockClient = Mockito.mock(EasyCodefClient.class);

		Field clientMapField = EasyCodef.class.getDeclaredField("clientMap");
		clientMapField.setAccessible(true);

		@SuppressWarnings("unchecked") Map<EasyCodefServiceType, EasyCodefClient> clientMap = (Map<EasyCodefServiceType, EasyCodefClient>)clientMapField
			.get(easyCodef);

		clientMap.put(EasyCodefServiceType.DEMO, mockClient);
	}

	@Test
	@DisplayName("[Success] 2-Way 추가인증 호출")
	void requestCertification_success() throws Exception {
		EasyCodefResponse firstMockResponse = Mockito.mock(EasyCodefResponse.class);
		when(firstMockResponse.toString()).thenReturn(TwoWayFixture.twoWayRequiredResponseJson());
		when(mockClient.requestProduct(any())).thenReturn(firstMockResponse);

		EasyCodefResponse secondMockResponse = Mockito.mock(EasyCodefResponse.class);
		when(secondMockResponse.toString()).thenReturn(TwoWayFixture.successResponseJson());
		when(mockClient.requestCertification(any())).thenReturn(secondMockResponse);

		HashMap<String, Object> paramMap = TwoWayFixture.firstRequestParams();

		String firstResult = easyCodef.requestProduct(
			"/v1/kr/mock/url",
			EasyCodefServiceType.DEMO,
			paramMap);

		JsonNode firstRoot = mapper.readTree(firstResult);
		String firstResponseCode = firstRoot.path("result").path("code").asText();

		JsonNode twoWayInfo = firstRoot.path("data").path("twoWayInfo");

		HashMap<String, Object> twoWayMap = new HashMap<>();
		twoWayMap.put("twoWayInfo", mapper.convertValue(twoWayInfo, HashMap.class));
		twoWayMap.put("is2Way", true);
		twoWayMap.put("simpleAuth", "1");

		String secondResult = easyCodef.requestCertification(
			"/v1/kr/mock/url",
			EasyCodefServiceType.DEMO,
			twoWayMap);

		JsonNode secondRoot = mapper.readTree(secondResult);

		assertAll(
			() -> assertEquals("CF-03002", firstResponseCode),
			() -> assertEquals("CF-00000", secondRoot.path("result").path("code").asText()));
	}
}
