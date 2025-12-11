package io.codef.api.fixture;

import java.util.List;

public class JsonUtilFixture {

	public String name;
	public int age;
	public List<String> items;

	private JsonUtilFixture() {}

	public JsonUtilFixture(String name, int age, List<String> items) {
		this.name = name;
		this.age = age;
		this.items = items;
	}
}
