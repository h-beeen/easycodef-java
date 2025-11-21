package io.codef.api;

public class EasyCodefHttpResponse {

    private final int statusCode;
    private final String body;

    public EasyCodefHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
