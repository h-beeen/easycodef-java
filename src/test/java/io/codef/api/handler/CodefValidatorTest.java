package io.codef.api.handler;

import static io.codef.api.constant.CodefConstant.*;
import static io.codef.api.constant.TwoWayConstant.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@DisplayName("[Handler Layer] CodefValidator Test")
public class CodefValidatorTest {

	@Nested
	@DisplayName("[isSuccessResponse] 해당 메서드가 정상 동작하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 유효한 객체인 경우 그대로 반환")
		void validateNotNullOrThrow_valid() {
			String input = "validString";

			String result = CodefValidator.validateNotNullOrThrow(input, CodefError.EMPTY_PARAMETER);

			assertEquals(input, result);
		}

		@Test
		@DisplayName("[Success] 유효한 경로인 경우 그대로 반환")
		void validatePathOrThrow_valid() {
			String path = "/v1/kr/bank/account/list";

			String result = CodefValidator.validatePathOrThrow(path);

			assertEquals(path, result);
		}

		@Test
		@DisplayName("[Success] Two-Way 키워드가 없는 경우 성공")
		void validateTwoWayKeywordsOrThrow_valid() {
			Map<String, Object> params = new HashMap<>();
			params.put("organization", "0004");
			params.put("loginType", "1");

			assertDoesNotThrow(() -> CodefValidator.validateTwoWayKeywordsOrThrow(params));
		}

		@Test
		@DisplayName("[Success] 모든 필수 정보가 존재하면 성공")
		void validateTwoWayInfoOrThrow_valid() {
			Map<String, Object> param = new HashMap<>();
			param.put(IS_2WAY.getValue(), true);

			Map<String, Object> info = new HashMap<>();
			info.put(JOB_INDEX.getValue(), 1);
			info.put(THREAD_INDEX.getValue(), 1);
			info.put(JTI.getValue(), "testJti");
			info.put(TIMESTAMP.getValue(), 1234567890L);
			param.put(INFO_KEY.getValue(), info);

			assertDoesNotThrow(() -> CodefValidator.validateTwoWayInfoOrThrow(param));
		}
	}

	@Nested
	@DisplayName("[Throw Exceptions] 예외처리가 정상 동작하면 성공")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] 객체가 null인 경우 지정된 에러로 예외처리")
		void validateNotNullOrThrow_null() {
			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateNotNullOrThrow(null, CodefError.EMPTY_PARAMETER));

			assertEquals(CodefError.EMPTY_PARAMETER, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 객체가 빈 문자열인 경우 지정된 에러로 예외처리")
		void validateNotNullOrThrow_invalid() {
			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateNotNullOrThrow("", CodefError.EMPTY_PARAMETER));

			assertEquals(CodefError.EMPTY_PARAMETER, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 경로가 null인 경우 INVALID_PATH_REQUESTED 예외처리")
		void validatePathOrThrow_null() {
			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validatePathOrThrow(null));

			assertEquals(CodefError.INVALID_PATH_REQUESTED, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 경로가 https로 시작하는 경우 INVALID_PATH_REQUESTED 예외처리")
		void validatePathOrThrow_invalid() {
			String path = "https://development.io/v1/kr/bank/account/list";

			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validatePathOrThrow(path));

			assertEquals(CodefError.INVALID_PATH_REQUESTED, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] is2Way 키워드가 포함된 경우 INVALID_2WAY_KEYWORD 예외처리")
		void validateTwoWayKeywordsOrThrow_containsIs2Way() {
			Map<String, Object> params = new HashMap<>();
			params.put("organization", "0004");
			params.put("is2Way", true);

			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayKeywordsOrThrow(params));

			assertEquals(CodefError.INVALID_2WAY_KEYWORD, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] twoWayInfo 키워드가 포함된 경우 INVALID_2WAY_KEYWORD 예외처리")
		void validateTwoWayKeywordsOrThrow_invalid() {
			Map<String, Object> params = new HashMap<>();
			params.put("organization", "0004");
			params.put("twoWayInfo", new HashMap<>());

			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayKeywordsOrThrow(params));

			assertEquals(CodefError.INVALID_2WAY_KEYWORD, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 파라미터가 비어있으면 EMPTY_PARAMETER 예외처리")
		void validateTwoWayInfoOrThrow_empty() {
			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayInfoOrThrow(new HashMap<>()));

			assertEquals(CodefError.EMPTY_PARAMETER, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] is2Way 값이 없거나 false이면 INVALID_2WAY_INFO 예외처리")
		void validateTwoWayInfoOrThrow_invalidIs2Way() {
			Map<String, Object> params = new HashMap<>();
			params.put("organization", "0004");

			CodefException emptyException = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayInfoOrThrow(params));

			params.put(IS_2WAY.getValue(), false);

			CodefException falseException = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayInfoOrThrow(params));

			assertAll(
				() -> assertEquals(CodefError.INVALID_2WAY_INFO, emptyException.getCodefError()),
				() -> assertEquals(CodefError.INVALID_2WAY_INFO, falseException.getCodefError()));
		}

		@Test
		@DisplayName("[Exception] twoWayInfo 값이 없으면 INVALID_2WAY_INFO 예외처리")
		void validateTwoWayInfoOrThrow_invalid2WayInfo() {
			Map<String, Object> params = new HashMap<>();
			params.put(IS_2WAY.getValue(), true);

			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayInfoOrThrow(params));

			assertEquals(CodefError.INVALID_2WAY_INFO, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] twoWayInfo 필수 키가 없으면 INVALID_2WAY_INFO 예외처리")
		void validateTwoWayInfoOrThrow_invalid2WayInfoKey() {
			Map<String, Object> params = new HashMap<>();
			params.put(IS_2WAY.getValue(), true);

			Map<String, Object> twoWayInfoMap = new HashMap<>();
			twoWayInfoMap.put(JOB_INDEX.getValue(), 1);
			params.put(INFO_KEY.getValue(), twoWayInfoMap);

			CodefException exception = assertThrows(CodefException.class,
				() -> CodefValidator.validateTwoWayInfoOrThrow(params));

			assertEquals(CodefError.INVALID_2WAY_INFO, exception.getCodefError());
		}
	}
}
