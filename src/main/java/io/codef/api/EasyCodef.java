package io.codef.api;

import io.codef.api.auth.EasyCodefTokenManager;
import io.codef.api.core.EasyCodefExecutor;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.handler.CodefValidator;

public class EasyCodef {

	private final EasyCodefProperties properties;
    private final EasyCodefExecutor executor;

    protected EasyCodef(EasyCodefBuilder builder) {
        EasyCodefProperties properties = new EasyCodefProperties(builder);
        EasyCodefTokenManager tokenManager = new EasyCodefTokenManager(properties);
        EasyCodefExecutor executor = new EasyCodefExecutor(tokenManager);

        this.properties = properties;
        this.executor = executor;
    }

	public String getPublicKey() {
		return properties.getPublicKey();
	}

    public String requestProduct(EasyCodefRequest request) {
        CodefValidator.validateTwoWayKeywordsOrThrow(request.getParameterMap());

        return executor.execute(request.getProductUrl(), properties.getServiceType(), request.getParameterMap());
    }

    public String requestCertification(EasyCodefRequest request) {
        CodefValidator.validateTwoWayInfoOrThrow(request.getParameterMap());

        return executor.execute(request.getProductUrl(), properties.getServiceType(), request.getParameterMap());
    }
}
