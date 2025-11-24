package io.codef.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.CodefHost;
import io.codef.api.constants.CodefPath;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.EasyCodefError;

public class EasyCodef {
	
	private final ObjectMapper mapper = new ObjectMapper();
	private final EasyCodefProperties properties = new EasyCodefProperties();

    private EasyCodefToken token;
    private EasyCodefToken demoToken;

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
		if(checkClientInfo(serviceType.getServiceType())) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.EMPTY_CLIENT_INFO);
            return mapper.writeValueAsString(response);
		}
		
		if(checkPublicKey()) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.EMPTY_PUBLIC_KEY);
            return mapper.writeValueAsString(response);
		}

		if(!checkTwoWayKeyword(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.INVALID_2WAY_KEYWORD);
            return mapper.writeValueAsString(response);
		}

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
		EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

		return mapper.writeValueAsString(response);
	}

    public String requestCertification(String productUrl, EasyCodefServiceType serviceType, HashMap<String, Object> parameterMap) throws JsonProcessingException {
        if (checkClientInfo(serviceType.getServiceType())) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.EMPTY_CLIENT_INFO);
            return mapper.writeValueAsString(response);
        }

        if (checkPublicKey()) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.EMPTY_PUBLIC_KEY);
            return mapper.writeValueAsString(response);
        }

        if (!checkTwoWayInfo(parameterMap)) {
            EasyCodefResponse response = ResponseHandler.fromError(EasyCodefError.INVALID_2WAY_INFO);
            return mapper.writeValueAsString(response);
        }

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
        EasyCodefResponse response = EasyCodefConnector.requestProduct(urlPath, accessToken, parameterMap);

        return mapper.writeValueAsString(response);
    }
	
	private boolean checkClientInfo(String serviceType) {
		if(Objects.equals(serviceType, CodefHost.API_DOMAIN)) {
			if(properties.getClientId() == null || properties.getClientId().trim().isEmpty()) {
				return true;
			}
            return properties.getClientSecret() == null || properties.getClientSecret().trim().isEmpty();
		} else {
			if(properties.getDemoClientId() == null || properties.getDemoClientId().trim().isEmpty()) {
				return true;
			}
            return properties.getDemoClientSecret() == null || properties.getDemoClientSecret().trim().isEmpty();
		}
    }

	private boolean checkPublicKey() {
        return properties.getPublicKey() == null || properties.getPublicKey().trim().isEmpty();
    }

	@SuppressWarnings("unchecked")
	private boolean checkTwoWayInfo(Map<String, Object> parameterMap) {
		if(!parameterMap.containsKey("is2Way") || !(parameterMap.get("is2Way") instanceof Boolean) || !(boolean)parameterMap.get("is2Way")){
			return false;
		}
		
		if(!parameterMap.containsKey("twoWayInfo")) {
			return false;
		}
		
		Map<String, Object> twoWayInfoMap = (Map<String, Object>)parameterMap.get("twoWayInfo");
        return twoWayInfoMap.containsKey("jobIndex") && twoWayInfoMap.containsKey("threadIndex") && twoWayInfoMap.containsKey("jti") && twoWayInfoMap.containsKey("twoWayTimestamp");
    }

	private boolean checkTwoWayKeyword(Map<String, Object> parameterMap) {
        return parameterMap == null || (!parameterMap.containsKey("is2Way") && !parameterMap.containsKey("twoWayInfo"));
    }

	public String createAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.CREATE_ACCOUNT, serviceType, parameterMap);
	}

	public String addAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.ADD_ACCOUNT, serviceType, parameterMap);
	}

	public String updateAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.UPDATE_ACCOUNT, serviceType, parameterMap);
	}

	public String deleteAccount(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.DELETE_ACCOUNT, serviceType, parameterMap);
	}

	public String getAccountList(EasyCodefServiceType serviceType, Map<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.GET_ACCOUNT_LIST, serviceType, parameterMap);
	}

	public String getConnectedIdList(EasyCodefServiceType serviceType) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
		return requestProduct(CodefPath.GET_CID_LIST, serviceType, null);
	}

	public String requestToken(EasyCodefServiceType serviceType) {
        EasyCodefToken token = getOrCreateToken(serviceType).validateAndRefreshToken();
        return token.getAccessToken();
	}

	public String requestNewToken(EasyCodefServiceType serviceType) {
        if (Objects.equals(serviceType.getServiceType(), CodefHost.API_DOMAIN)) {
            token = new EasyCodefToken(
                    properties.getClientId(), properties.getClientSecret()
            );
            return token.getAccessToken();
        } else {
            demoToken = new EasyCodefToken(
                    properties.getDemoClientId(), properties.getDemoClientSecret()
            );
            return demoToken.getAccessToken();
        }
	}

    private EasyCodefToken getOrCreateToken(EasyCodefServiceType serviceType) {
        if (Objects.equals(serviceType.getServiceType(), CodefHost.API_DOMAIN)) {
            if (token == null) {
                token = new EasyCodefToken(
                        properties.getClientId(), properties.getClientSecret()
                );
            }
            return token;
        } else {
            if (demoToken == null) {
                demoToken = new EasyCodefToken(
                        properties.getDemoClientId(), properties.getDemoClientSecret()
                );
            }
            return demoToken;
        }
    }
}
