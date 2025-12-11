package io.codef.api.root;

import static io.codef.api.error.CodefError.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.error.CodefException;

@DisplayName("[Root Layer] EasyCodef Test")
public class EasyCodefTest {

	private EasyCodef easyCodef;

	@BeforeEach
	void setUp() {
		easyCodef = new EasyCodef();
	}

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 설정되면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] Properties 설정 확인 (Getter Test)")
		void constructor_success() {
			String clientId = "test-id";
			String clientSecret = "test-secret";
			String publicKey = "test-public-key";

			easyCodef.setClientInfo(clientId, clientSecret);
			easyCodef.setPublicKey(publicKey);

			assertAll(
				() -> assertEquals(clientId, easyCodef.getClientId()),
				() -> assertEquals(clientSecret, easyCodef.getClientSecret()),
				() -> assertEquals(publicKey, easyCodef.getPublicKey()));
		}
	}

	@Nested
	@DisplayName("[Throw Exceptions] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] API 클라이언트 정보 미설정 시 EMPTY_CLIENT_ID 예외처리")
		void requestProduct_emptyClientInfo() {
			CodefException exception = assertThrows(CodefException.class,
				() -> easyCodef.requestProduct("/v1/test", EasyCodefServiceType.API, new HashMap<>()));

			assertEquals(EMPTY_CLIENT_ID, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] DEMO 클라이언트 정보 미설정 시 EMPTY_CLIENT_ID 예외처리")
		void requestProduct_emptyDemoClientInfo() {
			CodefException exception = assertThrows(CodefException.class,
				() -> easyCodef.requestProduct("/v1/test", EasyCodefServiceType.DEMO, new HashMap<>()));

			assertEquals(EMPTY_CLIENT_ID, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 퍼블릭 키 정보 미설정 시 EMPTY_PUBLIC_KEY 예외처리")
		void requestProduct_emptyPubKey() {
			easyCodef.setClientInfoForDemo("demo-id", "demo-secret");

			CodefException exception = assertThrows(CodefException.class,
				() -> easyCodef.requestProduct("/v1/test", EasyCodefServiceType.DEMO, new HashMap<>()));

			assertEquals(EMPTY_PUBLIC_KEY, exception.getCodefError());
		}
	}
}
