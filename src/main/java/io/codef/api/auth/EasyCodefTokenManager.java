package io.codef.api.auth;

import io.codef.api.EasyCodefProperties;

public class EasyCodefTokenManager {

	private final EasyCodefProperties properties;

	private EasyCodefToken token;

	public EasyCodefTokenManager(EasyCodefProperties properties) {
		this.properties = properties;
	}

	public String getValidAccessToken() {
		if (token == null) {
			token = createNewToken();
		}

		return token.validateAndRefreshToken().getAccessToken();
	}

	private EasyCodefToken createNewToken() {
		String clientId = properties.getClientId();
		String clientSecret = properties.getClientSecret();

		return new EasyCodefToken(clientId, clientSecret);
	}
}
