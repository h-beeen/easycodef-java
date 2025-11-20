package io.codef.api;

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


	public void setClientInfo(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
		this.demoClientId = demoClientId;
		this.demoClientSecret = demoClientSecret;
	}

	public String getDemoClientId() {
		return demoClientId;
	}

	public String getDemoClientSecret() {
		return demoClientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
