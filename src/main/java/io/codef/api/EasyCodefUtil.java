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
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.FileUtils;

public class EasyCodefUtil {

	public static String encryptRSA(String plainText, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] bytePublicKey = Base64.getDecoder().decode(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
		
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] bytePlain = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(bytePlain);
	}

	public static String encodeToFileString(String filePath) throws IOException {
		File file = new File(filePath);
		
		byte[] fileContent = FileUtils.readFileToByteArray(file);

        return Base64.getEncoder().encodeToString(fileContent);
	}
}
