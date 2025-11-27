package io.codef.api;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import org.apache.commons.io.FileUtils;

public class EasyCodefUtil {

	public static String encryptRSA(String plainText, String publicKey) {
		try {
            byte[] bytePublicKey = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes());

            return Base64.getEncoder().encodeToString(bytePlain);
        } catch (Exception e) {
            throw CodefException.of(CodefError.RSA_ENCRYPTION_ERROR, e);
        }
	}
}
