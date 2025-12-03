package io.codef.api;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;
import io.codef.api.service.EasyCodefDispatcher;

public class EasyCodef {

	private final EasyCodefDispatcher dispatcher;
	private final String publicKey;

	EasyCodef(EasyCodefDispatcher dispatcher, String publicKey) {
		this.dispatcher = dispatcher;
		this.publicKey = publicKey;
	}

	public EasyCodefResponse requestProduct(EasyCodefRequest request) {
		CodefValidator.validateTwoWayKeywordsOrThrow(request.getParameterMap());

		return dispatcher.dispatchRequest(request);
	}

	public EasyCodefResponse requestCertification(EasyCodefRequest request) {
		CodefValidator.validateTwoWayInfoOrThrow(request.getParameterMap());

		return dispatcher.dispatchRequest(request);
	}

	public String getPublicKey() {
		return publicKey;
	}
}
