package io.codef.api.auth;

import static io.codef.api.constants.CodefConstant.OAuth.*;

import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson2.JSONObject;

import io.codef.api.core.Client;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.util.AuthorizationUtil;

public class EasyCodefToken implements Token {

	private final Client client;
	private final String oauthToken;

	private String accessToken;
	private LocalDateTime expiresAt;

	public EasyCodefToken(String clientId, String clientSecret, Client client) {
		this.oauthToken = createOAuthToken(clientId, clientSecret);
		this.client = client;

		refreshToken();
	}

	@Override
	public String getValidAccessToken() {
		validateAndRefreshToken();
		return accessToken;
	}

	private void validateAndRefreshToken() {
		if (expiresAt == null || isTokenExpiringSoon(expiresAt)) {
			refreshToken();
		}
	}

	private String createOAuthToken(String clientId, String clientSecret) {
		String auth = clientId + ":" + clientSecret;
		byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());

		return new String(authEncBytes);
	}

	private void refreshToken() {
		String basicToken = AuthorizationUtil.createBasicAuth(oauthToken);
		EasyCodefResponse response = client.publishToken(basicToken);
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
}
