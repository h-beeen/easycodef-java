package io.codef.api;

import java.util.HashMap;
import java.util.Map;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@Deprecated
public class EasyCodef extends EasyCodefProperties {

	private final Map<EasyCodefServiceType, EasyCodefClient> clientMap = new HashMap<>();

	private EasyCodefClient easyCodefClient;

	public EasyCodef() {}

	@Deprecated
	public String requestProduct(
		String productUrl,
		EasyCodefServiceType serviceType,
		Map<String, Object> parameterMap) {
		this.easyCodefClient = easyCodefClient(serviceType);

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
		this.easyCodefClient = easyCodefClient(serviceType);

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
		this.easyCodefClient = easyCodefClient(serviceType);

		return easyCodefClient.requestToken();
	}

	@Deprecated
	public String requestNewToken(EasyCodefServiceType serviceType) {
		this.easyCodefClient = easyCodefClient(serviceType);

		return easyCodefClient.requestNewToken();
	}

	private EasyCodefClient easyCodefClient(EasyCodefServiceType serviceType) {
		if (clientMap.containsKey(serviceType)) {
			return clientMap.get(serviceType);
		}

		EasyCodefClient client = createClient(serviceType);

		clientMap.put(serviceType, client);

		return client;
	}

	private EasyCodefClient createClient(EasyCodefServiceType serviceType) {
		if (serviceType.equals(EasyCodefServiceType.API)) {
			return EasyCodefBuilder
				.builder()
				.serviceType(serviceType)
				.clientId(getClientId())
				.clientSecret(getClientSecret())
				.publicKey(getPublicKey())
				.build();
		} else if (serviceType.equals(EasyCodefServiceType.DEMO)) {
			return EasyCodefBuilder
				.builder()
				.serviceType(serviceType)
				.clientId(getDemoClientId())
				.clientSecret(getDemoClientSecret())
				.publicKey(getPublicKey())
				.build();
		} else {
			throw CodefException.from(CodefError.EMPTY_SERVICE_TYPE);
		}
	}
}
