package io.codef.api;

/**
 * <pre>
 * io.codef.easycodef
 *   |_ EasyCodefProperties.java
 * </pre>
 * 
 * Desc : 코드에프의 쉬운 사용을 위한 프로퍼티 클래스 
 * @Company : ©CODEF corp.
 * @Author  : notfound404@codef.io
 * @Date    : Jun 26, 2020 3:36:51 PM
 */
public class EasyCodefProperties {
	
	//	데모 엑세스 토큰 발급을 위한 클라이언트 아이디
	private String demoClientId 	= "";
	
	//	데모 엑세스 토큰 발급을 위한 클라이언트 시크릿
	private String demoClientSecret 	= "";
	
	//	정식 엑세스 토큰 발급을 위한 클라이언트 아이디
	private String clientId 	= "";
	
	//	정식 엑세스 토큰 발급을 위한 클라이언트 시크릿
	private String clientSecret 	= "";
	
	//	RSA암호화를 위한 퍼블릭키
	private String publicKey 	= "";

	
	/**
	 * Desc : 정식서버 사용을 위한 클라이언트 정보 설정
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:37:02 PM
	 * @param clientId
	 * @param clientSecret
	 */
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
	public String getDemoClientSecret() {
		return demoClientSecret;
	}

	/**
	 * Desc : 데모 클라이언트 시크릿 반환
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:37:36 PM
	 * @Version : 1.0.1
	 * @return
	 */
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
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Desc : RSA암호화를 위한 퍼블릭키 반환
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:37:59 PM
	 * @return
	 */
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
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
