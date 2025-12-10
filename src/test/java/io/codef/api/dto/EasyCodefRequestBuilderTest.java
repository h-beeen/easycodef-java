package io.codef.api.dto;

import static io.codef.api.error.CodefError.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.codef.api.error.CodefException;

@DisplayName("[Dto Layer] EasyCodefRequestBuilder Test")
public class EasyCodefRequestBuilderTest {

	@Test
	@DisplayName("[Success] Request 정상 생성 확인")
	void build_success() {
		String validUrl = "/v1/kr/bank/account/list";
		Map<String, Object> params = new HashMap<>();
		params.put("key", "value");

		EasyCodefRequest request = EasyCodefRequestBuilder.builder()
			.productUrl(validUrl)
			.parameterMap(params)
			.build();

		assertAll(
			() -> assertNotNull(request),
			() -> assertEquals(validUrl, request.getProductUrl()),
			() -> assertEquals(params, request.getParameterMap())
		);
	}

	@Test
	@DisplayName("[Exception] URL이 null이면 INVALID_PATH_REQUESTED 예외처리")
	void url_null() {
		EasyCodefRequestBuilder builder = EasyCodefRequestBuilder.builder();

		CodefException exception = assertThrows(CodefException.class, () ->
				builder.productUrl(null)
		);

		assertEquals(INVALID_PATH_REQUESTED, exception.getCodefError());
	}

	@Test
	@DisplayName("[Exception] URL이 https로 시작하면 INVALID_PATH_REQUESTED 예외처리")
	void url_https() {
		EasyCodefRequestBuilder builder = EasyCodefRequestBuilder.builder();

		CodefException exception = assertThrows(CodefException.class, () ->
				builder.productUrl("https://api.codef.io/v1/test")
		);

		assertEquals(INVALID_PATH_REQUESTED, exception.getCodefError());
	}

	@Test
	@DisplayName("[Exception] 파라미터 맵이 null이면 EMPTY_PARAMETER 예외처리")
	void fail_parameterMapNull() {
		EasyCodefRequestBuilder builder = EasyCodefRequestBuilder.builder();

		CodefException exception = assertThrows(CodefException.class, () ->
				builder.parameterMap(null)
		);

		assertEquals(EMPTY_PARAMETER, exception.getCodefError());
	}

	@Test
	@DisplayName("[Exception] build() 시 URL 미설정이면 INVALID_PATH_REQUESTED 예외처리")
	void build_missingUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("key", "value");

		EasyCodefRequestBuilder builder = EasyCodefRequestBuilder.builder()
			.parameterMap(params);

		CodefException exception = assertThrows(CodefException.class, builder::build);

		assertEquals(EMPTY_PATH, exception.getCodefError());
	}

	@Test
	@DisplayName("[Exception] build() 시 파라미터 맵 미설정이면 EMPTY_PARAMETER 예외처리")
	void build_missingParams() {
		EasyCodefRequestBuilder builder = EasyCodefRequestBuilder.builder()
			.productUrl("/v1/test/path");

		CodefException exception = assertThrows(CodefException.class, builder::build);

		assertEquals(EMPTY_PARAMETER, exception.getCodefError());
	}
}
