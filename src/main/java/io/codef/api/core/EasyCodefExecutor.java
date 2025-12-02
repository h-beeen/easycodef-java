package io.codef.api.core;

import java.util.Map;

import com.alibaba.fastjson2.JSON;

import io.codef.api.auth.Token;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.util.AuthorizationUtil;

public class EasyCodefExecutor implements Executor {

	private final Token token;
	private final CodefServiceType codefServiceType;
	private final Client client;

	public EasyCodefExecutor(Token token, CodefServiceType codefServiceType, Client client) {
		this.token = token;
		this.codefServiceType = codefServiceType;
		this.client = client;
	}

	@Override
	public EasyCodefResponse execute(EasyCodefRequest request) {
		String urlPath = codefServiceType.getHost() + request.getProductUrl();

		String accessToken = token.getValidAccessToken();
		String bearerToken = AuthorizationUtil.createBearerAuth(accessToken);

		Map<String, Object> parameterMap = request.getParameterMap();
		String jsonBody = JSON.toJSONString(parameterMap);

		Integer customTimeout = request.getCustomTimeout();

		return client.requestProduct(urlPath, bearerToken, jsonBody, customTimeout);
	}
}
