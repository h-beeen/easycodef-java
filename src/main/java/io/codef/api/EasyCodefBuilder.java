package io.codef.api;

import io.codef.api.auth.EasyCodefToken;
import io.codef.api.auth.Token;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.core.Client;
import io.codef.api.core.EasyCodefClient;
import io.codef.api.core.EasyCodefExecutor;
import io.codef.api.core.Executor;
import io.codef.api.error.CodefError;
import io.codef.api.handler.CodefValidator;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.HttpClient;

public class EasyCodefBuilder {

	private CodefServiceType serviceType;
	private String clientId;
	private String clientSecret;
	private String publicKey;
	private HttpClient httpClient;

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

	public EasyCodefBuilder httpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
		return this;
	}

	public EasyCodef build() {
		validateProperties();

		HttpClient httpClient = (this.httpClient == null)
			? new CodefHttpClient()
			: this.httpClient;

		Client client = new EasyCodefClient(httpClient);
		Token token = new EasyCodefToken(this.clientId, this.clientSecret, client);
		Executor executor = new EasyCodefExecutor(token, this.serviceType, client);

		return new EasyCodef(executor, this.publicKey);
	}

	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
	}
}
