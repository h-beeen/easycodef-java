package io.codef.api;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import io.codef.api.constant.CodefServiceType;
import io.codef.api.error.CodefError;
import io.codef.api.handler.CodefValidator;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.service.EasyCodefApiService;
import io.codef.api.service.EasyCodefOAuthService;

public class EasyCodefBuilder {

	private CodefServiceType serviceType;
	private String clientId;
	private String clientSecret;
	private String publicKey;
	private ApacheHttpClient httpClient;

	public static EasyCodefBuilder builder() {
		return new EasyCodefBuilder();
	}

	public EasyCodefBuilder serviceType(CodefServiceType serviceType) {
		this.serviceType = CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		return this;
	}

	public EasyCodefBuilder clientId(String clientId) {
		this.clientId = CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		return this;
	}

	public EasyCodefBuilder clientSecret(String clientSecret) {
		this.clientSecret = CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		return this;
	}

	public EasyCodefBuilder publicKey(String publicKey) {
		this.publicKey = CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
		return this;
	}

	public EasyCodefBuilder httpClient(CloseableHttpClient httpClient) {
		this.httpClient = ApacheHttpClient.ofCustom(httpClient);
		return this;
	}

	public EasyCodef build() {
		validateProperties();

		ApacheHttpClient httpClient = (this.httpClient == null)
			? ApacheHttpClient.of()
			: this.httpClient;

		EasyCodefOAuthService oAuthService = new EasyCodefOAuthService(httpClient);
		EasyCodefApiService apiService = new EasyCodefApiService(httpClient);

		EasyCodefToken token = new EasyCodefToken(clientId, clientSecret, oAuthService);
		EasyCodefDispatcher dispatcher = new EasyCodefDispatcher(token, serviceType, apiService);

		return new EasyCodef(dispatcher, publicKey);
	}

	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
	}
}
