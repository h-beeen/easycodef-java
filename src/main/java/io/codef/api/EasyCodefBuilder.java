package io.codef.api;

public class EasyCodefBuilder {

    private EasyCodefServiceType serviceType;
    private String clientId;
    private String clientSecret;
    private String publicKey;

    public static EasyCodefBuilder builder() {
        return new EasyCodefBuilder();
    }

    public EasyCodefBuilder serviceType(EasyCodefServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public EasyCodefBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public EasyCodefBuilder clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public EasyCodefBuilder publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
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

    public EasyCodef build() {
        return new EasyCodef(this);
    }
}
