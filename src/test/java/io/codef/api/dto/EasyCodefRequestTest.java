package io.codef.api.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Dto Layer] EasyCodefRequest Test")
public class EasyCodefRequestTest {

	@Test
	@DisplayName("[Success] 생성자 및 Getter가 올바르게 동작하는지 확인")
	public void success() {
		String expectedProductUrl = "/v1/test/product";
		Map<String, Object> expectedParameterMap = new HashMap<>();
		expectedParameterMap.put("param1", "value1");
		expectedParameterMap.put("param2", 123);

		EasyCodefRequest request = new EasyCodefRequest(expectedProductUrl, expectedParameterMap);

		assertAll(
			() -> assertNotNull(request),
			() -> assertEquals(expectedProductUrl, request.getProductUrl()),
			() -> assertSame(expectedParameterMap, request.getParameterMap())
		);

		request.getParameterMap().put("param3", "newValue");
		assertTrue(expectedParameterMap.containsKey("param3"));
	}
}
