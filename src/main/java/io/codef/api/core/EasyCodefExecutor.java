package io.codef.api.core;

import java.util.Map;

import com.alibaba.fastjson2.JSON;

import io.codef.api.auth.EasyCodefToken;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.util.AuthorizationUtil;

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
		String bearerToken = AuthorizationUtil.createBearerAuth(validToken.getAccessToken());

		Map<String, Object> parameterMap = request.getParameterMap();
		String jsonBody = JSON.toJSONString(parameterMap);

		Integer customTimeout = request.getCustomTimeout();

		return EasyCodefClient.requestProduct(urlPath, bearerToken, jsonBody, customTimeout);
	}
}
