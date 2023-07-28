package bg.sofia.uni.fmi.mjt.vault.exception;

public class IncorrectUsernameOrPasswordException extends RuntimeException {
    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }

    public IncorrectUsernameOrPasswordException(String message, Exception e) {
        super(message, e);
    }
}
