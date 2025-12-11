package io.codef.api.fixture;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefBuilder;
import io.codef.api.EasyCodefClient;
import io.codef.api.EasyCodefServiceType;

public class ClientInfoFixture {

	private static final String clientId = System.getenv("CLIENT_ID");
	private static final String clientSecret = System.getenv("CLIENT_SECRET");
	private static final String publicKey = System.getenv("PUBLIC_KEY");

	private ClientInfoFixture() {}

	public static EasyCodefClient demoClientFromEnv() {
		return EasyCodefBuilder.builder()
			.serviceType(EasyCodefServiceType.DEMO)
			.clientId(clientId)
			.clientSecret(clientSecret)
			.publicKey(publicKey)
			.build();
	}

	public static EasyCodef demoFromEnv() {
		EasyCodef easyCodef = new EasyCodef();
		easyCodef.setClientInfoForDemo(clientId, clientSecret);
		easyCodef.setPublicKey(publicKey);

		return easyCodef;
	}
}
