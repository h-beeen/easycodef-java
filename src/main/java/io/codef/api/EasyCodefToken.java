package io.codef.api;

import static io.codef.api.constant.OAuthConstant.*;

import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson2.JSONObject;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.service.EasyCodefOAuthService;
import io.codef.api.util.AuthorizationUtil;

public class EasyCodefToken {

	private final EasyCodefOAuthService oAuthService;
	private final String oauthToken;

	private String accessToken;
	private LocalDateTime expiresAt;

	EasyCodefToken(String clientId, String clientSecret, EasyCodefOAuthService oAuthService) {
		this.oauthToken = createOAuthToken(clientId, clientSecret);
		this.oAuthService = oAuthService;

		refreshToken();
	}

	public String getValidAccessToken() {
		validateAndRefreshToken();
		return AuthorizationUtil.createBearerAuth(accessToken);
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
		EasyCodefResponse response = oAuthService.publishToken(basicToken);
		JSONObject jsonObject = response.getData(JSONObject.class);

		Object accessToken = jsonObject.get(ACCESS_TOKEN.getValue());
		Object expiresIn = jsonObject.get(EXPIRES_IN.getValue());

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
