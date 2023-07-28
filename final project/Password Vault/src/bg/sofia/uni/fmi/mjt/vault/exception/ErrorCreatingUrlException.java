package bg.sofia.uni.fmi.mjt.vault.exception;

public class ErrorCreatingUrlException extends RuntimeException {
    public ErrorCreatingUrlException(String message) {
        super(message);
    }

    public ErrorCreatingUrlException(String message, Exception e) {
        super(message, e);
    }
}
