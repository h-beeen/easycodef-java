package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Util Layer] AuthorizationUtil Test")
public class AuthorizationUtilTest {

	@Nested
	@DisplayName("[isSuccessResponse] formatting를 정상적으로 처리하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] Basic formatting 성공")
		void basicFormatting_success() {
			String token = "someBase64EncodedToken";
			String emptyToken = "";

			assertAll(
				() -> assertEquals(
					"Basic someBase64EncodedToken",
					AuthorizationUtil.createBasicAuth(token)),
				() -> assertEquals(
					"Basic ",
					AuthorizationUtil.createBasicAuth(emptyToken)),
				() -> assertEquals(
					"Basic null",
					AuthorizationUtil.createBasicAuth(null)));
		}

		@Test
		@DisplayName("[Success] Bearer formatting 성공")
		void bearerFormatting_success() {
			String token = "someAccessTokenString";
			String emptyToken = "";

			assertAll(
				() -> assertEquals(
					"Bearer someAccessTokenString",
					AuthorizationUtil.createBearerAuth(token)),
				() -> assertEquals(
					"Bearer ",
					AuthorizationUtil.createBearerAuth(emptyToken)),
				() -> assertEquals(
					"Bearer null",
					AuthorizationUtil.createBearerAuth(null)));
		}
	}
}
