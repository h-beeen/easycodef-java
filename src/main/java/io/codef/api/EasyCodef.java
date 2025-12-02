package io.codef.api;

import io.codef.api.auth.EasyCodefToken;
import io.codef.api.core.EasyCodefExecutor;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;

public class EasyCodef {

	private final EasyCodefExecutor executor;
	private final String publicKey;

	protected EasyCodef(EasyCodefBuilder builder) {
		EasyCodefToken easyCodefToken = new EasyCodefToken(builder.getClientId(), builder.getClientSecret());

		this.publicKey = builder.getPublicKey();
		this.executor = new EasyCodefExecutor(easyCodefToken, builder.getServiceType());
	}

	public EasyCodefResponse requestProduct(EasyCodefRequest request) {
		CodefValidator.validateTwoWayKeywordsOrThrow(request.getParameterMap());

		return executor.execute(request);
	}

	public EasyCodefResponse requestCertification(EasyCodefRequest request) {
		CodefValidator.validateTwoWayInfoOrThrow(request.getParameterMap());

		return executor.execute(request);
	}

	public String getPublicKey() {
		return publicKey;
	}
}
