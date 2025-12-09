package io.codef.api.util;

import static io.codef.api.constant.CodefConstant.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

/**
 * RSA 암호화를 위한 유틸리티 클래스
 *
 */
public class RsaUtil {

	private RsaUtil() {
	}

	/**
	 * RSA 암호화 수행
	 *
	 * @param plainText 암호화할 평문
	 * @param publicKey RSA 암호화를 위한 퍼블릭 키
	 * @return 암호화된 문자열(Base64 인코딩)
	 * @throws CodefException 키 생성 또는 암호화 처리 중 오류가 발생한 경우 {@link CodefError#RSA_ENCRYPTION_ERROR}
	 */
	public static String encryptRsa(String plainText, String publicKey) {
		try {
			PublicKey key = generatePublicKey(publicKey);

			Cipher cipher = initializeCipher(key);
			byte[] bytePlain = cipher.doFinal(plainText.getBytes());

			return Base64.getEncoder().encodeToString(bytePlain);
		} catch (Exception e) {
			throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
		}
	}

	/**
	 * 문자열 형태의 퍼블릭 키를 {@link PublicKey} 객체로 변환
	 *
	 * @param publicKey RSA 암호화를 위한 퍼블릭 키
	 * @return PublicKey 객체
	 * @throws CodefException 퍼블릭 키 형식이 잘못되었거나 키 생성 과정에서 오류가 발생한 경우 {@link CodefError#RSA_ENCRYPTION_ERROR}
	 */
	private static PublicKey generatePublicKey(String publicKey) {
		final byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey);

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA.getValue());

			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedPublicKey));
		} catch (Exception e) {
			throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
		}
	}

	/**
	 * 암호화에 사용될 RSA {@link Cipher} 객체 초기화
	 *
	 * @param key PublicKey 객체
	 * @return Cipher 객체
	 * @throws CodefException Cipher 초기화 과정에서 오류가 발생한 경우 {@link CodefError#RSA_ENCRYPTION_ERROR}
	 */
	private static Cipher initializeCipher(PublicKey key) {
		try {
			Cipher cipher = Cipher.getInstance(RSA.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher;
		} catch (Exception e) {
			throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
		}
	}
}
