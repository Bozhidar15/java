package bg.sofia.uni.fmi.mjt.vault.exception;

public class SendMessageException extends RuntimeException {
    public SendMessageException(String message) {
        super(message);
    }

    public SendMessageException(String message, Exception e) {
        super(message, e);
    }
}
