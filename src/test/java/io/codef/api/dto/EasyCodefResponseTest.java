package io.codef.api.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Dto Layer] EasyCodefResponse Test")
public class EasyCodefResponseTest {

	private EasyCodefResponse.Result sampleResult;
	private Object sampleData;
	private Object sampleExtraInfo;

	@BeforeEach
	void setUp() {
		sampleResult = new EasyCodefResponse.Result(
			"CF-00000",
			"Success",
			"성공",
			"TXID1234567890");
		sampleData = new HashMap<String, String>() {
			{
				put("key1", "value1");
				put("key2", "value2");
			}
		};
		sampleExtraInfo = new HashMap<String, String>() {
			{
				put("extraKey", "extraValue");
			}
		};
	}

	@Nested
	@DisplayName("[FactoryMethod] 정적 팩토리 메서드 / 생성자 테스트")
	class FactoryMethod {

		@Test
		@DisplayName("[Success] data만 존재하는 경우")
		void from_success() {
			EasyCodefResponse response = EasyCodefResponse.from(sampleData);

			assertAll(
				() -> assertNull(response.getResult()),
				() -> assertEquals(sampleData, response.getData()),
				() -> assertNull(response.getExtraInfo()));
		}

		@Test
		@DisplayName("[Success] 모든 요소가 존재하는 경우")
		void of_all() {
			EasyCodefResponse response = EasyCodefResponse.of(sampleResult, sampleData, sampleExtraInfo);

			assertAll(
				() -> assertEquals(sampleResult, response.getResult()),
				() -> assertEquals(sampleData, response.getData()),
				() -> assertEquals(sampleExtraInfo, response.getExtraInfo()));
		}

		@Test
		@DisplayName("[Success] extraInfo가 null인 경우")
		void of_extraInfo_null() {
			EasyCodefResponse response = EasyCodefResponse.of(sampleResult, sampleData, null);

			assertAll(
				() -> assertEquals(sampleResult, response.getResult()),
				() -> assertEquals(sampleData, response.getData()),
				() -> assertNull(response.getExtraInfo()));
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] getter / setter가 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] getData 타입 변환 테스트")
		void getData_success() {
			EasyCodefResponse response = EasyCodefResponse.from(sampleData);

			Map<?, ?> data = response.getData(Map.class);

			assertAll(
				() -> assertEquals("value1", data.get("key1")),
				() -> assertEquals("value2", data.get("key2")));
		}

		@Test
		@DisplayName("[Success] toString() JSON 직렬화 테스트")
		void toString_success() {
			EasyCodefResponse responseWithExtra = EasyCodefResponse.of(sampleResult, sampleData, sampleExtraInfo);
			String jsonStringWithExtra = responseWithExtra.toString();

			EasyCodefResponse responseWithoutExtra = EasyCodefResponse.of(sampleResult, sampleData, null);
			String jsonStringWithoutExtra = responseWithoutExtra.toString();

			EasyCodefResponse responseFrom = EasyCodefResponse.from(sampleData);
			String jsonStringFrom = responseFrom.toString();

			assertAll(
				() -> assertNotNull(jsonStringWithoutExtra),
				() -> assertTrue(jsonStringWithoutExtra.contains("CF-00000")),
				() -> assertTrue(jsonStringWithoutExtra.contains("key1")),
				() -> assertFalse(jsonStringWithoutExtra.contains("extraKey")),

				() -> assertNotNull(jsonStringWithExtra),
				() -> assertTrue(jsonStringWithExtra.contains("CF-00000")),
				() -> assertTrue(jsonStringWithExtra.contains("key1")),
				() -> assertTrue(jsonStringWithExtra.contains("extraKey")),

				() -> assertNotNull(jsonStringFrom),
				() -> assertTrue(jsonStringFrom.contains("key1")));
		}

		@Test
		@DisplayName("[Success] Result 내부 클래스 생성자 및 Getter 테스트")
		void constructor_success() {
			EasyCodefResponse.Result result = new EasyCodefResponse.Result(
				"CF-00000",
				"Test Extra Message",
				"Test Message",
				"TEST_TID");

			assertAll(
				() -> assertEquals("CF-00000", result.getCode()),
				() -> assertEquals("Test Extra Message", result.getExtraMessage()),
				() -> assertEquals("Test Message", result.getMessage()),
				() -> assertEquals("TEST_TID", result.getTransactionId()));
		}

		@Test
		@DisplayName("[Success] Result 내부 클래스 Setter 테스트")
		void setter_success() {
			EasyCodefResponse.Result result = new EasyCodefResponse.Result();
			result.setCode("CF-20000");
			result.setExtraMessage("Updated Extra Message");
			result.setMessage("Updated Message");
			result.setTransactionId("UPDATED_TXID");

			assertAll(
				() -> assertEquals("CF-20000", result.getCode()),
				() -> assertEquals("Updated Extra Message", result.getExtraMessage()),
				() -> assertEquals("Updated Message", result.getMessage()),
				() -> assertEquals("UPDATED_TXID", result.getTransactionId()));
		}
	}
}
