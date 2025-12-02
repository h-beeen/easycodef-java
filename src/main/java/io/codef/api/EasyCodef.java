package io.codef.api;

import io.codef.api.core.Executor;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;

public class EasyCodef {

	private final Executor executor;
	private final String publicKey;

	EasyCodef(Executor executor, String publicKey) {
		this.executor = executor;
		this.publicKey = publicKey;
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
