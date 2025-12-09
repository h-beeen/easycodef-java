package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Util Layer] AuthorizationUtil Test")
public class AuthorizationUtilTest {

	@Nested
	@DisplayName("[isSuccessResponse] formatting를 정상적으로 처리하면 성공")
	class isSuccessResponse {

		@Test
		@DisplayName("[Success] Basic formatting 성공")
		void testBasicFormatting() {
			String token = "someBase64EncodedToken";
			String expectedAuthHeader = "Basic someBase64EncodedToken";
			String actualAuthHeader = AuthorizationUtil.createBasicAuth(token);
			assertEquals(expectedAuthHeader, actualAuthHeader);

			String emptyToken = "";
			String expectedEmptyAuthHeader = "Basic ";
			String actualEmptyAuthHeader = AuthorizationUtil.createBasicAuth(emptyToken);
			assertEquals(expectedEmptyAuthHeader, actualEmptyAuthHeader);

			String expectedNullAuthHeader = "Basic null";
			String actualNullAuthHeader = AuthorizationUtil.createBasicAuth(null);
			assertEquals(expectedNullAuthHeader, actualNullAuthHeader);
		}

		@Test
		@DisplayName("[Success] Bearer formatting 성공")
		void testBearerFormatting() {
			String token = "someAccessTokenString";
			String expectedAuthHeader = "Bearer someAccessTokenString";
			String actualAuthHeader = AuthorizationUtil.createBearerAuth(token);
			assertEquals(expectedAuthHeader, actualAuthHeader);

			String emptyToken = "";
			String expectedEmptyAuthHeader = "Bearer ";
			String actualEmptyAuthHeader = AuthorizationUtil.createBearerAuth(emptyToken);
			assertEquals(expectedEmptyAuthHeader, actualEmptyAuthHeader);

			String expectedNullAuthHeader = "Bearer null";
			String actualNullAuthHeader = AuthorizationUtil.createBearerAuth(null);
			assertEquals(expectedNullAuthHeader, actualNullAuthHeader);
		}
	}
}
