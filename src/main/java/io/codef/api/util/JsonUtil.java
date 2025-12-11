package io.codef.api.util;

import static io.codef.api.error.CodefError.*;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.error.CodefException;

public class JsonUtil {

	private static final ObjectMapper mapper =
		new ObjectMapper()
			.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

	private JsonUtil() {}

	public static String toJson(Object object) {
		if (object == null) {
			return null;
		}

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw CodefException.of(JSON_PARSE_ERROR, e);
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}

		try {
			return mapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw CodefException.of(JSON_PARSE_ERROR, e);
		}
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		if (fromValue == null) {
			return null;
		}

		return mapper.convertValue(fromValue, toValueType);
	}

	public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		if (fromValue == null) {
			return null;
		}

		return mapper.convertValue(fromValue, typeReference);
	}

	public static Map<String, Object> toMap(Object fromValue) {
		return convertValue(fromValue, new TypeReference<Map<String, Object>>() {});
	}
}
