package io.codef.api;

public class EasyCodefProperties {

    private final EasyCodefServiceType serviceType;
    private final String clientId;
    private final String clientSecret;
    private final String publicKey;

    protected EasyCodefProperties(EasyCodefBuilder builder) {
        this.serviceType = builder.getServiceType();
        this.clientId = builder.getClientId();
        this.clientSecret = builder.getClientSecret();
        this.publicKey = builder.getPublicKey();
    }

    public EasyCodefServiceType getServiceType() {
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

    protected boolean checkClientInfo() {
        String id = getClientId();
        String secret = getClientSecret();
        return isNullOrEmpty(id) || isNullOrEmpty(secret);
    }

    protected boolean checkPublicKey() {
        return isNullOrEmpty(publicKey);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
