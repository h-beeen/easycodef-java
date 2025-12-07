package io.codef.api.util;

/**
 * 인증 헤더 문자열 생성을 위한 유틸리티 클래스
 *
 * @author : kdso10
 * @since  : Dec 5, 2025
 */
public class AuthorizationUtil {

	private static final String BASIC_FORMAT = "Basic %s";
	private static final String BEARER_FORMAT = "Bearer %s";

	private AuthorizationUtil() {
	}

	/**
	 * Basic 인증 헤더 문자열 생성
	 *
	 * @param token Base64로 인코딩된 OAuth Token
	 * @return {@code "Basic {token}"} 형식의 인증 문자열
	 */
	public static String createBasicAuth(String token) {
		return String.format(BASIC_FORMAT, token);
	}

	/**
	 * Bearer 인증 헤더 문자열 생성
	 *
	 * @param token Access Token 문자열
	 * @return {@code "Bearer {token}"} 형식의 인증 문자열
	 */
	public static String createBearerAuth(String token) {
		return String.format(BEARER_FORMAT, token);
	}
}
