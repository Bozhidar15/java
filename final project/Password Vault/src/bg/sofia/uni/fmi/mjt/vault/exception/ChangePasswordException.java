package bg.sofia.uni.fmi.mjt.vault.exception;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException(String message) {
        super(message);
    }

    public ChangePasswordException(String message, Exception e) {
        super(message, e);
    }
}
