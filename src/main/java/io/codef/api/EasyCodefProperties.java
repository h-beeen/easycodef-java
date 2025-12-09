package io.codef.api;

/**
 * <pre>
 * io.codef.easycodef
 *   |_ EasyCodefProperties.java
 * </pre>
 *
 * 코드에프의 쉬운 사용을 위한 프로퍼티 클래스
 */
@Deprecated
public class EasyCodefProperties {

    //	데모 엑세스 토큰 발급을 위한 클라이언트 아이디
    @Deprecated
    private String demoClientId 	= "";

    //	데모 엑세스 토큰 발급을 위한 클라이언트 시크릿
    @Deprecated
    private String demoClientSecret 	= "";

    //	OAUTH2.0 데모 토큰
    @Deprecated
    private String demoAccessToken = "";

    @Deprecated
    //	정식 엑세스 토큰 발급을 위한 클라이언트 아이디
    private String clientId 	= "";

    //	정식 엑세스 토큰 발급을 위한 클라이언트 시크릿
    @Deprecated
    private String clientSecret 	= "";

    //	OAUTH2.0 토큰
    @Deprecated
    private String accessToken = "";

    //	RSA암호화를 위한 퍼블릭키
    @Deprecated
    private String publicKey 	= "";


    /**
     * Desc : 정식서버 사용을 위한 클라이언트 정보 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:02 PM
     * @param clientId
     * @param clientSecret
     */
    @Deprecated
    public void setClientInfo(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Desc : 데모서버 사용을 위한 클라이언트 정보 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:10 PM
     * @param demoClientId
     * @param demoClientSecret
     */
    @Deprecated
    public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
        this.demoClientId = demoClientId;
        this.demoClientSecret = demoClientSecret;
    }

    /**
     * Desc : 데모 클라이언트 아이디 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:17 PM
     * @return
     */
    @Deprecated
    public String getDemoClientId() {
        return demoClientId;
    }

    /**
     * Desc : 데모 클라이언트 시크릿 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:23 PM
     * @return
     */
    @Deprecated
    public String getDemoClientSecret() {
        return demoClientSecret;
    }

    /**
     * Desc : 데모 접속 토큰 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:30 PM
     * @return
     */
    @Deprecated
    public String getDemoAccessToken() {
        return demoAccessToken;
    }

    /**
     * Desc : 데모 클라이언트 시크릿 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:36 PM
     * @Version : 1.0.1
     * @return
     */
    @Deprecated
    public String getClientId() {
        return clientId;
    }

    /**
     * Desc : API 클라이언트 시크릿 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:44 PM
     * @return
     */
    @Deprecated
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Desc : API 접속 토큰 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:50 PM
     * @Version : 1.0.1
     * @return
     */
    @Deprecated
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Desc : RSA암호화를 위한 퍼블릭키 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:59 PM
     * @return
     */
    @Deprecated
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Desc : RSA암호화를 위한 퍼블릭키 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:38:07 PM
     * @Version : 1.0.1
     * @param publicKey
     */
    @Deprecated
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Desc : 데모 접속 토큰 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:38:14 PM
     * @param demoAccessToken
     */
    @Deprecated
    public void setDemoAccessToken(String demoAccessToken) {
        this.demoAccessToken = demoAccessToken;
    }

    /**
     * Desc : API 접속 토큰 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:38:21 PM
     * @param accessToken
     */
    @Deprecated
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
