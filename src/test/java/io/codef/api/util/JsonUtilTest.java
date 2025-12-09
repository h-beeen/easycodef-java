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

@DisplayName("[Util Layer] JsonUtil Test")
public class JsonUtilTest {

	static class TestPojo {
		public String name;
		public int age;
		public List<String> items;

		public TestPojo() {}

		public TestPojo(String name, int age, List<String> items) {
			this.name = name;
			this.age = age;
			this.items = items;
		}
	}

	@Nested
	@DisplayName("[isSuccessResponse] 정상적으로 변환하면 성공")
	class isSuccessResponse {

		@Test
		@DisplayName("[Success] 객체를 JSON 문자열로 변환")
		void testToJson() {
			TestPojo pojo = new TestPojo("Alice", 30, Arrays.asList("apple", "banana"));

			String json = JsonUtil.toJson(pojo);

			assertNotNull(json);
			assertTrue(json.contains("\"name\":\"Alice\""));
			assertTrue(json.contains("\"age\":30"));
			assertTrue(json.contains("\"items\":[\"apple\",\"banana\"]"));

			Map<String, Object> map = new HashMap<>();
			map.put("key", "value");
			map.put("number", 123);

			String mapJson = JsonUtil.toJson(map);

			assertNotNull(mapJson);
			assertTrue(mapJson.contains("\"key\":\"value\""));
			assertTrue(mapJson.contains("\"number\":123"));

			assertNull(JsonUtil.toJson(null));
		}

		@Test
		@DisplayName("[Success] JSON 문자열을 객체로 변환")
		void testFromJson() {
			String json = "{\"name\":\"Bob\",\"age\":25,\"items\":[\"orange\"]}";

			TestPojo pojo = JsonUtil.fromJson(json, TestPojo.class);

			assertNotNull(pojo);
			assertEquals("Bob", pojo.name);
			assertEquals(25, pojo.age);
			assertEquals(Collections.singletonList("orange"), pojo.items);

			Map<?, ?> map = JsonUtil.fromJson("{\"a\":1, \"b\":\"hello\"}", Map.class);

			assertNotNull(map);
			assertEquals(1, map.get("a"));
			assertEquals("hello", map.get("b"));

			assertNull(JsonUtil.fromJson(null, TestPojo.class));

			String invalidJson = "{'name':\"Charlie\"}";
			CodefException thrown = assertThrows(CodefException.class,
				() -> JsonUtil.fromJson(invalidJson, TestPojo.class));
			assertEquals(CodefError.JSON_PARSE_ERROR, thrown.getCodefError());
		}

		@Test
		@DisplayName("[Success] 객체를 다른 타입으로 변환")
		void testConvertWithClass() {
			Map<String, Object> map = new HashMap<>();
			map.put("name", "David");
			map.put("age", 40);
			map.put("items", Arrays.asList("pen", "book"));

			TestPojo pojo = JsonUtil.convertValue(map, TestPojo.class);

			assertNotNull(pojo);
			assertEquals("David", pojo.name);
			assertEquals(40, pojo.age);
			assertEquals(Arrays.asList("pen", "book"), pojo.items);

			assertNull(JsonUtil.convertValue(null, TestPojo.class));
		}

		@Test
		@DisplayName("[Success] TypeReference를 사용하여 객체 변환")
		void testConvertTypeReference() {
			Map<String, List<String>> mapWithList = new HashMap<>();
			mapWithList.put("myList", Arrays.asList("item1", "item2"));

			List<String> convertedList = JsonUtil.convertValue(
				mapWithList.get("myList"),
				new TypeReference<List<String>>() {
				});

			assertNotNull(convertedList);
			assertEquals(2, convertedList.size());
			assertTrue(convertedList.contains("item1"));
			assertTrue(convertedList.contains("item2"));

			assertNull(JsonUtil.convertValue(null, new TypeReference<List<String>>() {
			}));
		}

		@Test
		@DisplayName("[Success] 객체를 Map으로 변환")
		void testToMap() {
			TestPojo pojo = new TestPojo("Frank", 50, Arrays.asList("monitor", "keyboard"));

			Map<String, Object> map = JsonUtil.toMap(pojo);

			assertNotNull(map);
			assertEquals("Frank", map.get("name"));
			assertEquals(50, map.get("age"));
			assertEquals(Arrays.asList("monitor", "keyboard"), map.get("items"));

			Map<String, Object> originalMap = new HashMap<>();
			originalMap.put("num", 1);
			originalMap.put("str", "test");

			Map<String, Object> convertedMap = JsonUtil.toMap(originalMap);

			assertNotNull(convertedMap);
			assertEquals(1, convertedMap.get("num"));
			assertEquals("test", convertedMap.get("str"));

			assertNull(JsonUtil.toMap(null));
		}
	}
}
