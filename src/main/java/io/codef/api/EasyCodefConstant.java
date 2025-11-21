package io.codef.api;

public class EasyCodefConstant {
	
	/**	OAUTH 서버 도메인	*/
	protected static final String OAUTH_DOMAIN = "https://oauth.codef.io";
	
	/**	OAUTH 엑세스 토큰 발급 URL PATH	*/
	protected static final String GET_TOKEN = "/oauth/token?grant_type=client_credentials&scope=read";


	/**	데모 서버 도메인	*/
	protected static final String DEMO_DOMAIN = "https://development.codef.io";
	
	/**	정식 서버 도메인	*/
	protected static final String API_DOMAIN = "https://api.codef.io";
	
	
	/** 응답부 수행 결과 키워드	*/
	protected static final String RESULT = "result";
	
	/** 응답부 수행 결과 메시지 코드 키워드	*/
	protected static final String CODE = "code";

	/** 응답부 수행 결과 메시지 키워드	*/
	protected static final String MESSAGE = "message";
	
	/** 응답부 수행 결과 추가 메시지 키워드	*/
	protected static final String EXTRA_MESSAGE = "extraMessage";
	
	/**	응답부 수행 결과 데이터 키워드	*/
	protected static final String DATA = "data";

	/**	계정 등록 URL	*/
	protected static final String CREATE_ACCOUNT = "/v1/account/create";

	/**	계정 추가 URL	*/
	protected static final String ADD_ACCOUNT = "/v1/account/add";

	/**	계정 수정 URL	*/
	protected static final String UPDATE_ACCOUNT = "/v1/account/update";

	/**	계정 삭제 URL	*/
	protected static final String DELETE_ACCOUNT = "/v1/account/delete";

	/**	계정 목록 조회 URL	*/
	protected static final String GET_ACCOUNT_LIST = "/v1/account/list";

	/**	커넥티드 아이디 목록 조회 URL	*/
	protected static final String GET_CID_LIST = "/v1/account/connectedId-list";

}
