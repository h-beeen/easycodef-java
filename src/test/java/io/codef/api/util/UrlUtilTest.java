package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Util Layer] UrlUtil Test")
public class UrlUtilTest {

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 암호화하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] URL 디코딩하면 성공")
		void decode_Success() {
			String encoded1 = "Easy%20Codef";
			String decoded1 = "Easy Codef";

			String encoded2 = "%EC%9D%B4%EC%A7%80%EC%BD%94%EB%93%9C%EC%97%90%ED%94%84";
			String decoded2 = "이지코드에프";

			String actual1 = UrlUtil.decode(encoded1);
			String actual2 = UrlUtil.decode(encoded2);

			assertAll(
				() -> assertEquals(decoded1, actual1),
				() -> assertEquals(decoded2, actual2));
		}

		@Test
		@DisplayName("[Success] 이미 디코딩된 문자열 비교")
		void decode_AlreadyEncoded() {
			String decodedString = "Already Decoded String";

			String actualDecodedString = UrlUtil.decode(decodedString);

			assertEquals(decodedString, actualDecodedString);
		}
	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] 잘못된 인코딩 시퀀스인 경우 UNSUPPORTED_ENCODING 예외처리")
		void decode_unsupportedEncoding() {
			String malformedString = "%WRONG";

			CodefException exception = assertThrows(CodefException.class, () -> UrlUtil.decode(malformedString));

			assertEquals(CodefError.UNSUPPORTED_ENCODING, exception.getCodefError());
		}
	}
}
