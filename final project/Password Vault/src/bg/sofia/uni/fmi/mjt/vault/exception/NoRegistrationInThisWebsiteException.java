package bg.sofia.uni.fmi.mjt.vault.exception;

public class NoRegistrationInThisWebsiteException extends RuntimeException {
    public NoRegistrationInThisWebsiteException(String message) {
        super(message);
    }

    public NoRegistrationInThisWebsiteException(String message, Exception e) {
        super(message, e);
    }
}
