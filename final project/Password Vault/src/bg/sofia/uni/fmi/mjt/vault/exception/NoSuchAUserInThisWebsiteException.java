package bg.sofia.uni.fmi.mjt.vault.exception;

public class NoSuchAUserInThisWebsiteException extends RuntimeException {
    public NoSuchAUserInThisWebsiteException(String message) {
        super(message);
    }

    public NoSuchAUserInThisWebsiteException(String message, Exception e) {
        super(message, e);
    }
}
