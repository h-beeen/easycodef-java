package io.codef.api.e2e.fixture;

import java.util.HashMap;
import java.util.Map;

public class ParameterMapFixture {

	private ParameterMapFixture() {}

	public static Map<String, Object> floodedVehicleRequestParameterMap() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("organization", "0100");
		parameterMap.put("carNo", "12가1234");

		return parameterMap;
	}
}
