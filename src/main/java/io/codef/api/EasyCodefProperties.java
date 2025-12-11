package io.codef.api;

@Deprecated
public class EasyCodefProperties {

    //	데모 엑세스 토큰 발급을 위한 클라이언트 아이디
    @Deprecated
    private String demoClientId;

    //	데모 엑세스 토큰 발급을 위한 클라이언트 시크릿
    @Deprecated
    private String demoClientSecret;

    //	OAUTH2.0 데모 토큰
    @Deprecated
    private String demoAccessToken;

    @Deprecated
    //	정식 엑세스 토큰 발급을 위한 클라이언트 아이디
    private String clientId;

    //	정식 엑세스 토큰 발급을 위한 클라이언트 시크릿
    @Deprecated
    private String clientSecret;

    //	OAUTH2.0 토큰
    @Deprecated
    private String accessToken;

    //	RSA암호화를 위한 퍼블릭키
    @Deprecated
    private String publicKey;


    @Deprecated
    public void setClientInfo(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Deprecated
    public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
        this.demoClientId = demoClientId;
        this.demoClientSecret = demoClientSecret;
    }

    @Deprecated
    public String getDemoClientId() {
        return demoClientId;
    }

    @Deprecated
    public String getDemoClientSecret() {
        return demoClientSecret;
    }

    @Deprecated
    public String getDemoAccessToken() {
        return demoAccessToken;
    }

    @Deprecated
    public String getClientId() {
        return clientId;
    }

    @Deprecated
    public String getClientSecret() {
        return clientSecret;
    }

    @Deprecated
    public String getAccessToken() {
        return accessToken;
    }

    @Deprecated
    public String getPublicKey() {
        return publicKey;
    }

    @Deprecated
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Deprecated
    public void setDemoAccessToken(String demoAccessToken) {
        this.demoAccessToken = demoAccessToken;
    }

    @Deprecated
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
