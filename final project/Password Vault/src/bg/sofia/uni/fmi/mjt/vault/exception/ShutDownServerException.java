package bg.sofia.uni.fmi.mjt.vault.exception;

public class ShutDownServerException extends RuntimeException {
    public ShutDownServerException(String message) {
        super(message);
    }

    public ShutDownServerException(String message, Exception e) {
        super(message, e);
    }
}
