package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Util Layer] UrlUtil Test")
public class UrlUtilTest {

	@Test
	@DisplayName("[Success] URL 디코딩하면 성공")
	void testDecode_Success() {
		String encodedString = "Hello%20World%21";
		String expectedDecodedString = "Hello World!";

		String actualDecodedString = UrlUtil.decode(encodedString);

		assertEquals(expectedDecodedString, actualDecodedString);

		encodedString = "%ED%95%9C%EA%B8%80";
		expectedDecodedString = "한글";

		actualDecodedString = UrlUtil.decode(encodedString);

		assertEquals(expectedDecodedString, actualDecodedString);
	}

	@Test
	@DisplayName("[Success] 이미 디코딩된 문자열 비교")
	void testDecode_AlreadyEncoded() {
		String decodedString = "Already Decoded String";

		String actualDecodedString = UrlUtil.decode(decodedString);

		assertEquals(decodedString, actualDecodedString);
	}

	@Test
	@DisplayName("[Exception] 잘못된 인코딩 시퀀스인 경우 UNSUPPORTED_ENCODING 예외처리")
	void testDecode_UnsupportedEncoding() {
		String malformedString = "%WRONG";

		CodefException exception = assertThrows(CodefException.class, () ->
				UrlUtil.decode(malformedString)
		);

		assertEquals(CodefError.UNSUPPORTED_ENCODING, exception.getCodefError());
	}
}
