package io.codef.api.dto;

import com.alibaba.fastjson2.JSONObject;

public class EasyCodefResponse {

    private final Result result;
    private final Object data;

    public EasyCodefResponse(Result result, Object data) {
        this.result = result;
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public <T> T getData(Class<T> clazz) {
        return JSONObject.from(data).to(clazz);
    }

    public static class Result {
        private final String code;
        private final String extraMessage;
        private final String message;
        private final String transactionId;

        public Result(String code,
                      String extraMessage,
                      String message,
                      String transactionId) {
            this.code = code;
            this.extraMessage = extraMessage;
            this.message = message;
            this.transactionId = transactionId;
        }

        public String getCode() {
            return code;
        }

        public String getExtraMessage() {
            return extraMessage;
        }

        public String getMessage() {
            return message;
        }

        public String getTransactionId() {
            return transactionId;
        }
    }
}
