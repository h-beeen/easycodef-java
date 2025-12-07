package io.codef.api.service;

import static io.codef.api.constant.CodefHost.*;
import static io.codef.api.constant.CodefPath.*;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;
import io.codef.api.http.HttpRequestBuilder;

/**
 * CODEF OAuth 토큰 발급 서비스 클래스
 *
 * @author : kdso10
 * @since  : Dec 5, 2025
 */
public class EasyCodefOAuthService extends EasyCodefService {

	public EasyCodefOAuthService(CodefHttpClient httpClient) {
		super(httpClient);
	}

	/**
	 * 토큰 발급 요청 실행
	 *
	 * @param basicToken Client ID와 Secret으로 생성된 Basic 인증 문자열
	 * @return 발급된 토큰 정보를 포함한 응답 객체
	 */
	public EasyCodefResponse requestToken(String basicToken) {
		CodefHttpRequest request = HttpRequestBuilder.builder()
			.url(OAUTH_DOMAIN + GET_TOKEN)
			.header("Authorization", basicToken)
			.build();

		return sendRequest(request);
	}
}
