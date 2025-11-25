package io.codef.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.constants.CodefPath;
import io.codef.api.constants.EasyCodefServiceType;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

import static io.codef.api.EasyCodefUtil.mapper;
import static io.codef.api.EasyCodefUtil.mapTypeRef;

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

	public String requestProduct(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        EasyCodefResponse validationError = validateCommonRequirements(serviceType);
        if (validationError != null) {
            return mapper().writeValueAsString(validationError);
        }

		if(!checkTwoWayKeyword(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.INVALID_2WAY_KEYWORD);
            return mapper().writeValueAsString(response);
		}

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
		EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

		return mapper().writeValueAsString(response);
	}

    public String requestCertification(String productUrl, EasyCodefServiceType serviceType, HashMap<String, Object> parameterMap) throws JsonProcessingException {
        EasyCodefResponse validationError = validateCommonRequirements(serviceType);
        if (validationError != null) {
            return mapper().writeValueAsString(validationError);
        }

        if (!checkTwoWayInfo(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.INVALID_2WAY_INFO);
            return mapper().writeValueAsString(response);
        }

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
        EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

        return mapper().writeValueAsString(response);
    }

    private EasyCodefResponse validateCommonRequirements(EasyCodefServiceType serviceType) {
        if (properties.checkClientInfo(serviceType)) {
            return ResponseHandler.fromError(EasyCodefError.EMPTY_CLIENT_INFO);
        }

        if (properties.checkPublicKey()) {
            return ResponseHandler.fromError(EasyCodefError.EMPTY_PUBLIC_KEY);
        }

        return null;
    }

	private boolean checkTwoWayInfo(Map<String, Object> parameterMap) {
        Object is2WayObj = parameterMap.get("is2Way");
        if (!(is2WayObj instanceof Boolean) || !((Boolean) is2WayObj)) {
            return false;
        }

        Object twoWayInfoObj = parameterMap.get("twoWayInfo");
        if (!(twoWayInfoObj instanceof Map)) {
            return false;
        }

        try {
            Map<String, Object> twoWayInfoMap = mapper().convertValue(twoWayInfoObj, mapTypeRef());

            return twoWayInfoMap.containsKey("jobIndex")
                    && twoWayInfoMap.containsKey("threadIndex")
                    && twoWayInfoMap.containsKey("jti")
                    && twoWayInfoMap.containsKey("twoWayTimestamp");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

	private boolean checkTwoWayKeyword(Map<String, Object> parameterMap) {
        return parameterMap == null || (!parameterMap.containsKey("is2Way") && !parameterMap.containsKey("twoWayInfo"));
    }

	public String createAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.CREATE_ACCOUNT, serviceType, parameterMap);
	}

	public String requestToken(EasyCodefServiceType serviceType) {
        return tokenManager.getAccessToken(serviceType);
	}

	public String requestNewToken(EasyCodefServiceType serviceType) {
        return tokenManager.getNewAccessToken(serviceType);
	}
}
