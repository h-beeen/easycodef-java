package io.codef.api;

import io.codef.api.error.CodefError;

public class EasyCodefBuilder {

    private EasyCodefServiceType serviceType;
    private String clientId;
    private String clientSecret;
    private String publicKey;

    public static EasyCodefBuilder builder() {
        return new EasyCodefBuilder();
    }

    public EasyCodefBuilder serviceType(EasyCodefServiceType serviceType) {
        this.serviceType = EasyCodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
        return this;
    }

    public EasyCodefBuilder clientId(String clientId) {
        this.clientId = EasyCodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
        return this;
    }

    public EasyCodefBuilder clientSecret(String clientSecret) {
        this.clientSecret = EasyCodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
        return this;
    }

    public EasyCodefBuilder publicKey(String publicKey) {
        this.publicKey = EasyCodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
        return this;
    }

    public EasyCodef build() {
        validateProperties();

        return new EasyCodef(this);
    }

    protected EasyCodefServiceType getServiceType() {
        return serviceType;
    }

    protected String getClientId() {
        return clientId;
    }

    protected String getClientSecret() {
        return clientSecret;
    }

    protected String getPublicKey() {
        return publicKey;
    }

    private void validateProperties() {
        EasyCodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
        EasyCodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
        EasyCodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
        EasyCodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
    }
}
