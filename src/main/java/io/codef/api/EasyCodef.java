package io.codef.api;

import java.util.HashMap;
import java.util.Map;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;

@Deprecated
public class EasyCodef extends EasyCodefProperties {

	private final EasyCodefClientRegistry registry;

	public EasyCodef() {
		this.registry = new EasyCodefClientRegistry();
	}

	@Deprecated
	public String requestProduct(
		String productUrl,
		EasyCodefServiceType serviceType,
		Map<String, Object> parameterMap) {
		EasyCodefClient easyCodefClient = registry.getOrCreate(this, serviceType);

		EasyCodefRequest request = EasyCodefRequestBuilder
			.builder()
			.productUrl(productUrl)
			.parameterMap(parameterMap)
			.build();

		EasyCodefResponse response = easyCodefClient.requestProduct(request);

		return response.toString();
	}

	@Deprecated
	public String requestCertification(
		String productUrl,
		EasyCodefServiceType serviceType,
		HashMap<String, Object> parameterMap) {
		EasyCodefClient easyCodefClient = registry.getOrCreate(this, serviceType);

		EasyCodefRequest request = EasyCodefRequestBuilder
			.builder()
			.productUrl(productUrl)
			.parameterMap(parameterMap)
			.build();

		EasyCodefResponse response = easyCodefClient.requestCertification(request);

		return response.toString();
	}

	@Deprecated
	public String requestToken(EasyCodefServiceType serviceType) {
		EasyCodefClient easyCodefClient = registry.getOrCreate(this, serviceType);

		return easyCodefClient.requestToken();
	}

	@Deprecated
	public String requestNewToken(EasyCodefServiceType serviceType) {
		EasyCodefClient easyCodefClient = registry.getOrCreate(this, serviceType);

		return easyCodefClient.requestNewToken();
	}
}
