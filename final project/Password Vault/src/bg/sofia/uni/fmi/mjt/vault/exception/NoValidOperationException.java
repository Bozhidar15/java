package bg.sofia.uni.fmi.mjt.vault.exception;

public class NoValidOperationException extends RuntimeException {
    public NoValidOperationException(String message) {
        super(message);
    }

    public NoValidOperationException(String message, Exception e) {
        super(message, e);
    }
}
