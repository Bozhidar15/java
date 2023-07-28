package bg.sofia.uni.fmi.mjt.vault.exception;

public class DisconnectOrLogoutException extends RuntimeException {
    public DisconnectOrLogoutException(String message) {
        super(message);
    }

    public DisconnectOrLogoutException(String message, Exception e) {
        super(message, e);
    }
}
