package io.codef.api.constants;

public enum CodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);
	
	private final String host;
	
	CodefServiceType(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
}
