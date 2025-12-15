package io.codef.api.util;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

/**
 * JSON 직렬화/역직렬화를 위한 유틸리티 클래스
 *
 */
public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper()
		.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

	private JsonUtil() {}

	/**
	 * 객체를 JSON 문자열로 변환
	 *
	 * @param object JSON으로 변환할 객체
	 * @return 변환된 JSON 문자열
	 * @throws CodefException JSON 직렬화에 실패한 경우 {@link CodefError#JSON_PARSE_ERROR}
	 */
	public static String toJson(Object object) {
		if (object == null) {
			return null;
		}

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw CodefException.of(CodefError.JSON_PARSE_ERROR, e);
		}
	}

	/**
	 * JSON 문자열을 지정한 타입의 객체로 변환
	 *
	 * @param json JSON 문자열
	 * @param clazz 변환할 대상 클래스
	 * @param <T> 변환 대상 타입
	 * @return 변환된 객체
	 * @throws CodefException JSON 역직렬화에 실패한 경우 {@link CodefError#JSON_PARSE_ERROR}
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}

		try {
			return mapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw CodefException.of(CodefError.JSON_PARSE_ERROR, e);
		}
	}

	/**
	 * 객체를 지정한 타입으로 변환
	 *
	 * @param fromValue 변환할 원본 객체
	 * @param toValueType 변환 대상 클래스
	 * @param <T> 변환 대상 타입
	 * @return 변환된 객체
	 */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		if (fromValue == null) {
			return null;
		}

		return mapper.convertValue(fromValue, toValueType);
	}

	/**
	 * 객체를 {@link TypeReference} 기반 타입으로 변환
	 *
	 * @param fromValue 변환할 원본 객체
	 * @param typeReference 변환 대상 타입 정보
	 * @param <T> 변환 대상 타입
	 * @return 변환된 객체
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		if (fromValue == null) {
			return null;
		}

		return mapper.convertValue(fromValue, typeReference);
	}

	/**
	 * 객체를 {@link Map} 형태로 변환
	 *
	 * @param fromValue 변환할 원본 객체
	 * @return 변환된 Map 객체
	 */
	public static Map<String, Object> toMap(Object fromValue) {
		return convertValue(fromValue, new TypeReference<Map<String, Object>>() {});
	}
}
