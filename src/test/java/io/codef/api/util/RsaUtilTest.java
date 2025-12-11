package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Util Layer] RsaUtil Test")
public class RsaUtilTest {

	private static String validPublicKey;

	@BeforeAll
	static void setUp() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		validPublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
	}

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 암호화하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 암호화 처리하면 성공")
		void encryptRsa_success() {
			String plainText = "Sensitive Data";
			String encryptedText = RsaUtil.encryptRsa(plainText, validPublicKey);

			assertAll(
				() -> assertNotNull(encryptedText),
				() -> assertFalse(encryptedText.isEmpty()),
				() -> assertNotEquals(plainText, encryptedText),
				() -> assertDoesNotThrow(() -> Base64.getDecoder().decode(encryptedText)));
		}
	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] 잘못된 Public Key 입력 시 RSA_ENCRYPTION_ERROR 예외처리")
		void encryptRsa_invalidKey() {
			String plainText = "Sensitive Data";
			String invalidPublicKey = "NotARealKey";

			CodefException exception = assertThrows(CodefException.class,
				() -> RsaUtil.encryptRsa(plainText, invalidPublicKey));

			assertEquals(CodefError.RSA_ENCRYPTION_ERROR, exception.getCodefError());
		}
	}
}
