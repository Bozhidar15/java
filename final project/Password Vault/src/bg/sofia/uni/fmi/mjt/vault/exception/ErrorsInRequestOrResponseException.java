package bg.sofia.uni.fmi.mjt.vault.exception;

public class ErrorsInRequestOrResponseException extends RuntimeException {
    public ErrorsInRequestOrResponseException(String message) {
        super(message);
    }

    public ErrorsInRequestOrResponseException(String message, Exception e) {
        super(message, e);
    }
}
