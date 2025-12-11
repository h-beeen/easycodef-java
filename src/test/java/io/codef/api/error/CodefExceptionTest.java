package io.codef.api.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Error Layer] CodefException Test")
public class CodefExceptionTest {

	@Nested
	@DisplayName("[FactoryMethod] 정적 팩토리 메서드 / 생성자 테스트")
	class FactoryMethod {

		@Test
		@DisplayName("[Success] ErrorCode만 존재하는 경우")
		void from_success() {
			CodefError codefError = CodefError.EMPTY_CLIENT_ID;

			CodefException exception = CodefException.from(codefError);

			assertAll(
				() -> assertNotNull(exception),
				() -> assertEquals(codefError, exception.getCodefError()),
				() -> assertEquals(codefError.getMessage(), exception.getMessage()),
				() -> assertNull(exception.getCause()));
		}

		@Test
		@DisplayName("[Success] ErrorCode, Exception이 존재하는 경우")
		void of_exception() {
			CodefError codefError = CodefError.IO_ERROR;
			Exception cause = new RuntimeException("Network issue");

			CodefException exception = CodefException.of(codefError, cause);

			assertAll(
				() -> assertNotNull(exception),
				() -> assertEquals(codefError, exception.getCodefError()),
				() -> assertTrue(exception.getMessage().contains(codefError.getMessage())),
				() -> assertTrue(exception.getMessage().contains(cause.getMessage())),
				() -> assertEquals(cause, exception.getCause()));
		}

		@Test
		@DisplayName("[Success] ErrorCode, extraMessage가 존재하는 경우")
		void of_extraMessage() {
			CodefError codefError = CodefError.INVALID_PATH_REQUESTED;
			String extraMessage = "The path 'invalid/path' was used.";

			CodefException exception = CodefException.of(codefError, extraMessage);

			assertAll(
				() -> assertNotNull(exception),
				() -> assertEquals(codefError, exception.getCodefError()),
				() -> assertTrue(exception.getMessage().contains(codefError.getMessage())),
				() -> assertTrue(exception.getMessage().contains(extraMessage)),
				() -> assertNull(exception.getCause()));
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 메시지 조합이 성공적으로 조합되면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 메시지 조합이 %s\n%s로 formatting되면 성공")
		void decoratedMessage_success() {
			CodefError codefError = CodefError.UNSUPPORTED_ENCODING;

			CodefException exception = CodefException.from(codefError);

			Exception cause = new IllegalArgumentException("Invalid argument format");
			CodefException exceptionWithCause = CodefException.of(codefError, cause);
			String expectedMessageWithCause = codefError.getMessage() + System.lineSeparator() + cause.getMessage();

			String extraInfo = "Specific file not found";
			CodefException exceptionWithExtraMessage = CodefException.of(codefError, extraInfo);
			String expectedMessageWithExtraInfo = codefError.getMessage() + System.lineSeparator() + extraInfo;

			assertAll(
				() -> assertEquals(expectedMessageWithCause, exceptionWithCause.getMessage()),
				() -> assertEquals(codefError.getMessage(), exception.getMessage()),
				() -> assertEquals(expectedMessageWithExtraInfo, exceptionWithExtraMessage.getMessage()));
		}
	}
}
