package io.codef.api.util;

import static io.codef.api.constants.CodefConstant.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

public class RsaUtil {

	private RsaUtil() {
	}

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

	private static PublicKey generatePublicKey(String publicKey) {
		final byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey);

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);

			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedPublicKey));
		} catch (Exception e) {
			throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
		}
	}

	private static Cipher initializeCipher(PublicKey key) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher;
		} catch (Exception e) {
			throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
		}
	}
}
