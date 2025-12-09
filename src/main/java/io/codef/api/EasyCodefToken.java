package io.codef.api;

import static io.codef.api.constant.OAuthConstant.*;

import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson2.JSONObject;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.service.EasyCodefOAuthService;
import io.codef.api.util.AuthorizationUtil;

/**
 * CODEF OAuth Access Token 관리를 위한 클래스
 *
 */
public class EasyCodefToken {

	private final EasyCodefOAuthService oAuthService;
	private final String oauthToken;

	private String accessToken;
	private LocalDateTime expiresAt;

	/**
	 * EasyCodefToken 생성자
	 *
	 * <p>
	 *     OAuth 토큰을 생성하고, Access Token을 발급하여 초기화
	 * </p>
	 *
	 * @param clientId        CODEF 클라이언트 아이디
	 * @param clientSecret    CODEF 클라이언트 시크릿
	 * @param oAuthService    OAuth 토큰 발급 요청 담당 서비스
	 */
	EasyCodefToken(String clientId, String clientSecret, EasyCodefOAuthService oAuthService) {
		this.oauthToken = createOAuthToken(clientId, clientSecret);
		this.oAuthService = oAuthService;

		refreshToken();
	}

	/**
	 * 유효한 Access Token 반환
	 *
	 * @return "Bearer {accessToken}" 형식의 인증 문자열
	 */
	public String getValidAccessToken() {
		validateAndRefreshToken();
		return AuthorizationUtil.createBearerAuth(accessToken);
	}

	String getAccessToken() {
		validateAndRefreshToken();
		return accessToken;
	}

	String getNewAccessToken() {
		refreshToken();
		return accessToken;
	}

	/**
	 * Access Token 만료 여부 검증, 필요시 재발급
	 */
	private void validateAndRefreshToken() {
		if (expiresAt == null || isTokenExpiringSoon(expiresAt)) {
			refreshToken();
		}
	}

	/**
	 * 클라이언트 아이디, 시크릿을 이용해 OAuth 토큰 생성
	 *
	 * @param clientId        CODEF 클라이언트 아이디
	 * @param clientSecret    CODEF 클라이언트 시크릿
	 * @return Base64 인코딩된 OAuth 토큰 문자열
	 */
	private String createOAuthToken(String clientId, String clientSecret) {
		String auth = clientId + ":" + clientSecret;
		byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());

		return new String(authEncBytes);
	}

	/**
	 * CODEF OAuth API로부터 Access Token 발급하여 초기화
	 */
	private void refreshToken() {
		String basicToken = AuthorizationUtil.createBasicAuth(oauthToken);
		EasyCodefResponse response = oAuthService.requestToken(basicToken);
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

	/**
	 * 토큰 만료 시간 임박 여부 확인
	 *
	 * @param expiry 토큰 만료 시각
	 * @return 만료까지 24시간 미만이면 {@code true}
	 */
	private boolean isTokenExpiringSoon(LocalDateTime expiry) {
		return expiry.isBefore(LocalDateTime.now().plusHours(24));
	}
}
