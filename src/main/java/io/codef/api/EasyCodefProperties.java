package io.codef.api;

public class EasyCodefProperties {
	
	private String demoClientId 	= "";
	
	private String demoClientSecret 	= "";
	
	private String clientId 	= "";
	
	private String clientSecret 	= "";
	
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
