package io.codef.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.util.JsonUtil;

import static io.codef.api.constants.CodefPath.CREATE_ACCOUNT;

public class EasyCodef {

	private final EasyCodefProperties properties;
    private final EasyCodefTokenManager tokenManager;

    protected EasyCodef(EasyCodefBuilder builder) {
        this.properties = new EasyCodefProperties(builder);
        this.tokenManager = new EasyCodefTokenManager(properties);
    }

	public String getPublicKey() {
		return properties.getPublicKey();
	}

    public String requestProduct(EasyCodefRequest request) {
        return requestProduct(request.getProductUrl(), request.getServiceType(), request.getParameterMap());
    }

	public String requestProduct(String productUrl, EasyCodefServiceType serviceType, Map<String, Object> parameterMap) {
        EasyCodefValidator.validateRequest(properties);

		if(!EasyCodefValidator.checkTwoWayKeyword(parameterMap)) {
            throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
		}

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
		EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);

		return JsonUtil.writeValueAsString(response);
	}

    public String requestCertification(String productUrl, EasyCodefServiceType serviceType, HashMap<String, Object> parameterMap) {
        EasyCodefValidator.validateRequest(properties);

        if (!EasyCodefValidator.checkTwoWayInfo(parameterMap)) {
            throw CodefException.from(CodefError.INVALID_2WAY_INFO);
        }

        String accessToken = requestToken(serviceType);
        String urlPath = serviceType.getServiceType() + productUrl;
        EasyCodefResponse response = EasyCodefApiClient.requestProduct(urlPath, accessToken, parameterMap);

        return JsonUtil.writeValueAsString(response);
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
