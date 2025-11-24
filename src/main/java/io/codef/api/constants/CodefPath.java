package io.codef.api.constants;

public class CodefPath {

    public static final String GET_TOKEN = "/oauth/token?grant_type=client_credentials&scope=read";
    public static final String CREATE_ACCOUNT = "/v1/account/create";
    public static final String ADD_ACCOUNT = "/v1/account/add";
    public static final String UPDATE_ACCOUNT = "/v1/account/update";
    public static final String DELETE_ACCOUNT = "/v1/account/delete";
    public static final String GET_ACCOUNT_LIST = "/v1/account/list";
    public static final String GET_CID_LIST = "/v1/account/connectedId-list";
}
