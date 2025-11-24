package io.codef.api;

import io.codef.api.constants.CodefHost;

public enum EasyCodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);
	
	private final String serviceType;
	
	EasyCodefServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceType() {
		return serviceType;
	}
}
