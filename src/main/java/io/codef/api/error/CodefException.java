package io.codef.api.error;

public class CodefException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static String decoratedMessage (
            final String errorMessage, final String message) {
        return String.format("%s\n%s", errorMessage, message);
    }

    private CodefException(CodefError codefError) {
        super(codefError.getMessage());
    }

    private CodefException(CodefError codefError, Exception exception) {
        super(decoratedMessage(codefError.getMessage(), exception.getMessage()), exception);
    }

    private CodefException(CodefError codefError, String extraMessage) {
        super(decoratedMessage(codefError.getMessage(), extraMessage));
    }

    public static CodefException from(CodefError codefError) {
        return new CodefException(codefError);
    }

    public static CodefException of(CodefError codefError, Exception exception) {
        return new CodefException(codefError, exception);
    }

    public static CodefException of(CodefError codefError, String extraMessage) {
        return new CodefException(codefError, extraMessage);
    }
}
