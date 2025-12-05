package io.codef.api.util;

/**
 * Desc :
 *
 * @author : kdso10@codef.io
 * @since  : Dec 5, 2025
 */
public class AuthorizationUtil {

	private static final String BASIC_FORMAT = "Basic %s";
	private static final String BEARER_FORMAT = "Bearer %s";

	private AuthorizationUtil() {
	}

	public static String createBasicAuth(String token) {
		return String.format(BASIC_FORMAT, token);
	}

	public static String createBearerAuth(String token) {
		return String.format(BEARER_FORMAT, token);
	}
}
