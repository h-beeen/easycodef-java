package io.codef.api.e2e.fixture;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefClient;
import io.codef.api.EasyCodefServiceType;

public class TokenFixture {

	public static Object getTokenObject(EasyCodef easyCodef, EasyCodefServiceType serviceType) throws Exception {
		Field clientMapField = EasyCodef.class.getDeclaredField("clientMap");
		clientMapField.setAccessible(true);

		Map<?, ?> clientMap = (Map<?, ?>) clientMapField.get(easyCodef);
		Object client = clientMap.get(serviceType);

		Field dispatcherField = EasyCodefClient.class.getDeclaredField("dispatcher");
		dispatcherField.setAccessible(true);
		Object dispatcher = dispatcherField.get(client);

		Field field = dispatcher.getClass().getDeclaredField("token");
		field.setAccessible(true);
		return field.get(dispatcher);
	}

	public static Object getTokenObjectInClient(EasyCodefClient easyCodefClient) throws Exception {
		Field dispatcherField = EasyCodefClient.class.getDeclaredField("dispatcher");
		dispatcherField.setAccessible(true);
		Object dispatcher = dispatcherField.get(easyCodefClient);

		Field field = dispatcher.getClass().getDeclaredField("token");
		field.setAccessible(true);
		return field.get(dispatcher);
	}

	public static String getAccessToken(Object tokenObj) throws Exception {
		Field field = tokenObj.getClass().getDeclaredField("accessToken");
		field.setAccessible(true);
		return (String) field.get(tokenObj);
	}

	public static void forceExpireToken(Object tokenObj) throws Exception {
		Field field = tokenObj.getClass().getDeclaredField("expiresAt");
		field.setAccessible(true);
		field.set(tokenObj, LocalDateTime.now().minusHours(1));
	}
}
