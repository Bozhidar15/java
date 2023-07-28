package bg.sofia.uni.fmi.mjt.vault.exception;

public class NoPasswordException extends RuntimeException {
    public NoPasswordException(String message) {
        super(message);
    }

    public NoPasswordException(String message, Exception e) {
        super(message, e);
    }
}
