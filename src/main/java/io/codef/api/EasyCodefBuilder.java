package io.codef.api;

import io.codef.api.constants.CodefServiceType;
import io.codef.api.error.CodefError;
import io.codef.api.handler.CodefValidator;

public class EasyCodefBuilder {

	private CodefServiceType serviceType;
	private String clientId;
	private String clientSecret;
	private String publicKey;

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

	public EasyCodef build() {
		validateProperties();

		return new EasyCodef(this);
	}

	protected CodefServiceType getServiceType() {
		return serviceType;
	}

	protected String getClientId() {
		return clientId;
	}

	protected String getClientSecret() {
		return clientSecret;
	}

	protected String getPublicKey() {
		return publicKey;
	}

	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
	}
}
