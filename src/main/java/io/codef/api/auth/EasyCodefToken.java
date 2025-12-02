package io.codef.api.auth;

import static io.codef.api.constants.CodefConstant.OAuth.*;

import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson2.JSONObject;

import io.codef.api.core.EasyCodefApiClient;
import io.codef.api.dto.EasyCodefResponse;

public class EasyCodefToken {

	private final String oauthToken;

	private String accessToken;
	private LocalDateTime expiresAt;

	EasyCodefToken(String clientId, String clientSecret) {
		this.oauthToken = createOAuthToken(clientId, clientSecret);

		EasyCodefResponse response = EasyCodefApiClient.publishToken(oauthToken);

		initializeToken(response);
	}

	EasyCodefToken validateAndRefreshToken() {
		if (expiresAt == null || isTokenExpiringSoon(expiresAt)) {
			refreshToken();
		}

		return this;
	}

	String getAccessToken() {
		return accessToken;
	}

	private String createOAuthToken(String clientId, String clientSecret) {
		String auth = clientId + ":" + clientSecret;
		byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());

		return new String(authEncBytes);
	}

	private void initializeToken(EasyCodefResponse response) {
		JSONObject jsonObject = response.getData(JSONObject.class);

		Object accessToken = jsonObject.get(ACCESS_TOKEN);
		Object expiresIn = jsonObject.get(EXPIRES_IN);

		if (accessToken == null || expiresIn == null) {
			return;
		}

		this.accessToken = String.valueOf(accessToken);
		this.expiresAt = LocalDateTime.now()
			.plusSeconds(Integer.parseInt(String.valueOf(expiresIn)));
	}

	private boolean isTokenExpiringSoon(LocalDateTime expiry) {
		return expiry.isBefore(LocalDateTime.now().plusHours(24));
	}

	private void refreshToken() {
		EasyCodefResponse response = EasyCodefApiClient.publishToken(oauthToken);

		initializeToken(response);
	}
}
