package io.codef.api;

import io.codef.api.constants.CodefServiceType;

public class EasyCodefProperties {

    private final CodefServiceType serviceType;
    private final String clientId;
    private final String clientSecret;
    private final String publicKey;

    protected EasyCodefProperties(EasyCodefBuilder builder) {
        this.serviceType = builder.getServiceType();
        this.clientId = builder.getClientId();
        this.clientSecret = builder.getClientSecret();
        this.publicKey = builder.getPublicKey();
    }

    public CodefServiceType getServiceType() {
        return serviceType;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
