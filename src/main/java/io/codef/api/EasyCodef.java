package io.codef.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import static io.codef.api.constants.CodefPath.CREATE_ACCOUNT;
import static io.codef.api.util.JsonUtil.mapper;

public class EasyCodef {

	private final EasyCodefProperties properties = new EasyCodefProperties();
    private final EasyCodefTokenManager tokenManager = new EasyCodefTokenManager(properties);

	public void setClientInfo(String clientId, String clientSecret) {
		properties.setClientInfo(clientId, clientSecret);
	}

	public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
		properties.setClientInfoForDemo(demoClientId, demoClientSecret);
	}

	public void setPublicKey(String publicKey) {
		properties.setPublicKey(publicKey);
	}

	public String getPublicKey() {
		return properties.getPublicKey();
	}

	public String requestProduct(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws JsonProcessingException {
        EasyCodefValidator.validateRequest(properties, serviceType);

		if(!EasyCodefValidator.checkTwoWayKeyword(parameterMap)) {
            throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
		}

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
		EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);

		return mapper().writeValueAsString(response);
	}

    public String requestCertification(String productUrl, EasyCodefServiceType serviceType, HashMap<String, Object> parameterMap) throws JsonProcessingException {
        EasyCodefValidator.validateRequest(properties, serviceType);

        if (!EasyCodefValidator.checkTwoWayInfo(parameterMap)) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
        EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);

        return mapper().writeValueAsString(response);
    }

	public String createAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws JsonProcessingException {
		return requestProduct(CREATE_ACCOUNT, serviceType, parameterMap);
	}

	public String requestToken(EasyCodefServiceType serviceType) {
        return tokenManager.getAccessToken(serviceType);
	}

	public String requestNewToken(EasyCodefServiceType serviceType) {
        return tokenManager.getNewAccessToken(serviceType);
	}
}
