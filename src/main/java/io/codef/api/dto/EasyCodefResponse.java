package io.codef.api.dto;

public class EasyCodefResponse {

    public static final String RESULT = "result";
    public static final String DATA   = "data";

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

    public String code() {
        return (result != null) ? result.getCode() : null;
    }

    public String transactionId() {
        return (result != null) ? result.getTransactionId() : null;
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
