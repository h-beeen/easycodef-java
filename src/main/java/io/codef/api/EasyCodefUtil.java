package io.codef.api;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA 알고리즘을 사용할 수 없습니다.", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("잘못된 PublicKey 형식입니다.", e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException("Cipher Padding 설정 오류입니다.", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("PublicKey가 올바르지 않습니다.", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("암호화 처리 중 BlockSize 오류가 발생했습니다.", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("암호화 처리 중 Padding 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("RSA 암호화 중 알 수 없는 오류가 발생했습니다.", e);
        }
	}

	public static String encodeToFileString(String filePath) {
        try {
            File file = new File(filePath);

            byte[] fileContent = FileUtils.readFileToByteArray(file);

            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다: " + filePath, e);
        }
	}
}
