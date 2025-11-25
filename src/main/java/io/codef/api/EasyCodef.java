package io.codef.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.constants.CodefPath;
import io.codef.api.constants.CodefServiceType;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import static io.codef.api.EasyCodefValidator.*;
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

	public String requestProduct(String productUrl, CodefServiceType serviceType, Map<String, Object> parameterMap) throws JsonProcessingException {
        EasyCodefResponse validationError = validateCommonRequirements(properties, serviceType);
        if (validationError != null) {
            return mapper().writeValueAsString(validationError);
        }

		if(!checkTwoWayKeyword(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.handleErrorResponse(EasyCodefError.INVALID_2WAY_KEYWORD);
            return mapper().writeValueAsString(response);
		}

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
		EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

		return mapper().writeValueAsString(response);
	}

    public String requestCertification(String productUrl, CodefServiceType serviceType, HashMap<String, Object> parameterMap) throws JsonProcessingException {
        EasyCodefResponse validationError = validateCommonRequirements(properties, serviceType);
        if (validationError != null) {
            return mapper().writeValueAsString(validationError);
        }

        if (!checkTwoWayInfo(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.handleErrorResponse(EasyCodefError.INVALID_2WAY_INFO);
            return mapper().writeValueAsString(response);
        }

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
        EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

        return mapper().writeValueAsString(response);
    }

	public String createAccount(CodefServiceType serviceType, Map<String, Object> parameterMap) throws JsonProcessingException {
		return requestProduct(CodefPath.CREATE_ACCOUNT, serviceType, parameterMap);
	}

	public String requestToken(CodefServiceType serviceType) {
        return tokenManager.getAccessToken(serviceType);
	}

	public String requestNewToken(CodefServiceType serviceType) {
        return tokenManager.getNewAccessToken(serviceType);
	}
}
