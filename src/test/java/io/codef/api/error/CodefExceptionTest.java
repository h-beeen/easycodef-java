package io.codef.api.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[Error Layer] CodefException Test")
public class CodefExceptionTest {

	@Nested
	@DisplayName("[factoryMethod] 정적 팩토리 메서드 / 생성자 테스트")
	class factoryMethod {

		@Test
		@DisplayName("[Success] ErrorCode만 존재하는 경우")
		void testFromFactoryMethod() {
			CodefError codefError = CodefError.EMPTY_CLIENT_ID;
			CodefException exception = CodefException.from(codefError);

			assertNotNull(exception);
			assertEquals(codefError, exception.getCodefError());
			assertEquals(codefError.getMessage(), exception.getMessage());
			assertNull(exception.getCause());
		}

		@Test
		@DisplayName("[Success] ErrorCode, Exception이 존재하는 경우")
		void testFromFactoryMethodWithCause() {
			CodefError codefError = CodefError.IO_ERROR;
			Exception cause = new RuntimeException("Network issue");
			CodefException exception = CodefException.of(codefError, cause);

			assertNotNull(exception);
			assertEquals(codefError, exception.getCodefError());
			assertTrue(exception.getMessage().contains(codefError.getMessage()));
			assertTrue(exception.getMessage().contains(cause.getMessage()));
			assertEquals(cause, exception.getCause());
		}

		@Test
		@DisplayName("[Success] ErrorCode, extraMessage가 존재하는 경우")
		void testFromFactoryMethodWithExtraMessage() {
			CodefError codefError = CodefError.INVALID_PATH_REQUESTED;
			String extraMessage = "The path 'invalid/path' was used.";
			CodefException exception = CodefException.of(codefError, extraMessage);

			assertNotNull(exception);
			assertEquals(codefError, exception.getCodefError());
			assertTrue(exception.getMessage().contains(codefError.getMessage()));
			assertTrue(exception.getMessage().contains(extraMessage));
			assertNull(exception.getCause());
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 메시지 조합이 성공적으로 조합되면 성공")
	class isSuccessResponse {

		@Test
		@DisplayName("[Success] 메시지 조합이 %s\n%s로 formatting되면 성공")
		void testDecoratedMessage() {
			CodefError codefError = CodefError.UNSUPPORTED_ENCODING;
			CodefException exception = CodefException.from(codefError);
			assertEquals(codefError.getMessage(), exception.getMessage());

			Exception cause = new IllegalArgumentException("Invalid argument format");
			CodefException exceptionWithCause = CodefException.of(codefError, cause);
			String expectedMessageWithCause = codefError.getMessage() + System.lineSeparator() + cause.getMessage();
			assertEquals(expectedMessageWithCause, exceptionWithCause.getMessage());

			String extraInfo = "Specific file not found";
			CodefException exceptionWithExtraMessage = CodefException.of(codefError, extraInfo);
			String expectedMessageWithExtraInfo = codefError.getMessage() + System.lineSeparator() + extraInfo;
			assertEquals(expectedMessageWithExtraInfo, exceptionWithExtraMessage.getMessage());
		}
	}
}
