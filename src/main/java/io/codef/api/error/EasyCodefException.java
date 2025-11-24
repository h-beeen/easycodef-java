package io.codef.api.error;

public class EasyCodefException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final EasyCodefError error;

    private EasyCodefException(EasyCodefError error, String extraMessage) {
        super(buildMessage(error, extraMessage, null));
        this.error = error;
    }

    private EasyCodefException(EasyCodefError error, Throwable cause) {
        super(buildMessage(error, null, cause), cause);
        this.error = error;
    }

    private static String buildMessage(EasyCodefError error, String extraMessage, Throwable cause) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(error.getCode()).append("] ")
                .append(error.getMessage());

        if (extraMessage != null && !extraMessage.isEmpty()) {
            sb.append("\nextra: ").append(extraMessage);
        }

        if (cause != null && cause.getMessage() != null && !cause.getMessage().isEmpty()) {
            sb.append("\n→ cause: ").append(cause.getMessage());
        }

        return sb.toString();
    }

    public static EasyCodefException of(EasyCodefError error, String extraMessage) {
        return new EasyCodefException(error, extraMessage);
    }

    public static EasyCodefException of(EasyCodefError error, Throwable cause) {
        return new EasyCodefException(error, cause);
    }
}
