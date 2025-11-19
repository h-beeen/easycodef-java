package io.codef.api;

/**
 * <pre>
 * io.codef.easycodef
 *   |_ EasyCodefServiceType.java
 * </pre>
 * 
 * Desc : CODEF 서비스 타입 enum 클래스
 * @Company : ©CODEF corp.
 * @Author  : notfound404@codef.io
 * @Date    : Jun 26, 2020 3:40:36 PM
 */
public enum EasyCodefServiceType {
	DEMO(1),
	API(0);
	
	private final int serviceType;
	
	EasyCodefServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getServiceType() {
		return serviceType;
	}
}
