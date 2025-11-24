package io.codef.api.dto;

import java.util.Objects;

public final class EasyCodefResponse {

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

    /**
     * 편의 메서드: 결과 코드
     */
    public String code() {
        return (result != null) ? result.getCode() : null;
    }

    /**
     * 편의 메서드: 트랜잭션 ID
     */
    public String transactionId() {
        return (result != null) ? result.getTransactionId() : null;
    }

    @Override
    public String toString() {
        return "EasyCodefResponse{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasyCodefResponse)) return false;
        EasyCodefResponse that = (EasyCodefResponse) o;
        return Objects.equals(result, that.result) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, data);
    }

    /**
     * result 영역 값 객체
     */
    public static final class Result {

        private final String code;
        private final String extraMessage;
        private final String message;
        private final String transactionId;

        public Result(
                String code,
                String extraMessage,
                String message,
                String transactionId
        ) {
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

        @Override
        public String toString() {
            return "Result{" +
                    "code='" + code + '\'' +
                    ", extraMessage='" + extraMessage + '\'' +
                    ", message='" + message + '\'' +
                    ", transactionId='" + transactionId + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Result)) return false;
            Result result = (Result) o;
            return Objects.equals(code, result.code) &&
                    Objects.equals(extraMessage, result.extraMessage) &&
                    Objects.equals(message, result.message) &&
                    Objects.equals(transactionId, result.transactionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, extraMessage, message, transactionId);
        }
    }
}
