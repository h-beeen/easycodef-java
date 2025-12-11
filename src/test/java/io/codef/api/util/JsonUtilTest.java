package io.codef.api.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.fixture.JsonUtilFixture;

@DisplayName("[Util Layer] JsonUtil Test")
public class JsonUtilTest {

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 변환하면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] 객체를 JSON 문자열로 변환")
		void toJson_success() {
			JsonUtilFixture pojo = new JsonUtilFixture("Alice", 30, Arrays.asList("apple", "banana"));

			Map<String, Object> map = new HashMap<>();
			map.put("key", "value");
			map.put("number", 123);

			String json = JsonUtil.toJson(pojo);
			String mapJson = JsonUtil.toJson(map);

			Map<?, ?> pojoMap = JsonUtil.fromJson(json, Map.class);
			Map<?, ?> convertedMap = JsonUtil.fromJson(mapJson, Map.class);

			assertAll(
				() -> assertEquals("Alice", pojoMap.get("name")),
				() -> assertEquals(30, ((Number)pojoMap.get("age")).intValue()),
				() -> assertEquals(Arrays.asList("apple", "banana"), pojoMap.get("items")),

				() -> assertEquals(map.get("key"), convertedMap.get("key")),
				() -> assertEquals(map.get("number"), convertedMap.get("number")));
		}

		@Test
		@DisplayName("[Success] JSON 문자열을 객체로 변환")
		void fromJson_success() {
			Map<String, Object> pojoSource = new HashMap<>();
			pojoSource.put("name", "Bob");
			pojoSource.put("age", 25);
			pojoSource.put("items", Collections.singletonList("orange"));

			Map<String, Object> mapSource = new HashMap<>();
			mapSource.put("a", 1);
			mapSource.put("b", "hello");

			String pojoJson = JsonUtil.toJson(pojoSource);
			String mapJson = JsonUtil.toJson(mapSource);
			String invalidJson = "{'name':\"Charlie\"}";

			JsonUtilFixture pojo = JsonUtil.fromJson(pojoJson, JsonUtilFixture.class);
			Map<?, ?> map = JsonUtil.fromJson(mapJson, Map.class);
			CodefException thrown = assertThrows(
				CodefException.class,
				() -> JsonUtil.fromJson(invalidJson, JsonUtilFixture.class));

			assertAll(
				() -> assertNotNull(pojo),
				() -> assertEquals("Bob", pojo.name),
				() -> assertEquals(25, pojo.age),
				() -> assertEquals(Collections.singletonList("orange"), pojo.items),

				() -> assertNotNull(map),
				() -> assertEquals(1, map.get("a")),
				() -> assertEquals("hello", map.get("b")),
				() -> assertEquals(CodefError.JSON_PARSE_ERROR, thrown.getCodefError()));
		}

		@Test
		@DisplayName("[Success] 객체를 다른 타입으로 변환")
		void convertWithClass_success() {
			Map<String, Object> map = new HashMap<>();
			map.put("name", "David");
			map.put("age", 40);
			map.put("items", Arrays.asList("pen", "book"));

			JsonUtilFixture pojo = JsonUtil.convertValue(map, JsonUtilFixture.class);

			assertAll(
				() -> assertNotNull(pojo),
				() -> assertEquals("David", pojo.name),
				() -> assertEquals(40, pojo.age),
				() -> assertEquals(Arrays.asList("pen", "book"), pojo.items));
		}

		@Test
		@DisplayName("[Success] TypeReference를 사용하여 객체 변환")
		void convertTypeReference_success() {
			Map<String, List<String>> mapWithList = new HashMap<>();
			mapWithList.put("myList", Arrays.asList("item1", "item2"));

			List<String> convertedList = JsonUtil.convertValue(
				mapWithList.get("myList"),
				new TypeReference<List<String>>() {});

			assertAll(
				() -> assertNotNull(convertedList),
				() -> assertEquals(2, convertedList.size()),
				() -> assertTrue(convertedList.containsAll(Arrays.asList("item1", "item2"))));
		}

		@Test
		@DisplayName("[Success] 객체를 Map으로 변환")
		void toMap_success() {
			JsonUtilFixture pojo = new JsonUtilFixture("Frank", 50, Arrays.asList("monitor", "keyboard"));

			Map<String, Object> originalMap = new HashMap<>();
			originalMap.put("num", 1);
			originalMap.put("str", "test");

			Map<String, Object> map = JsonUtil.toMap(pojo);
			Map<String, Object> convertedMap = JsonUtil.toMap(originalMap);

			assertAll(
				() -> assertNotNull(map),
				() -> assertEquals("Frank", map.get("name")),
				() -> assertEquals(50, map.get("age")),
				() -> assertEquals(Arrays.asList("monitor", "keyboard"), map.get("items")),

				() -> assertNotNull(convertedMap),
				() -> assertEquals(1, convertedMap.get("num")),
				() -> assertEquals("test", convertedMap.get("str")));
		}
	}
}
