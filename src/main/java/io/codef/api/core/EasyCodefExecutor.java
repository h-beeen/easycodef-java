package io.codef.api.core;

import java.util.Map;

import io.codef.api.auth.EasyCodefToken;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;

public class EasyCodefExecutor {

	private final EasyCodefToken easyCodefToken;
	private final CodefServiceType codefServiceType;

	public EasyCodefExecutor(EasyCodefToken easyCodefToken, CodefServiceType codefServiceType) {
		this.easyCodefToken = easyCodefToken;
		this.codefServiceType = codefServiceType;
	}

	public EasyCodefResponse execute(EasyCodefRequest request) {
		String urlPath = codefServiceType.getHost() + request.getProductUrl();
		EasyCodefToken validToken = easyCodefToken.validateAndRefreshToken();
		Map<String, Object> parameterMap = request.getParameterMap();
		Integer customTimeout = request.getCustomTimeout();

		return EasyCodefClient.requestProduct(urlPath, validToken, parameterMap, customTimeout);
	}
}
