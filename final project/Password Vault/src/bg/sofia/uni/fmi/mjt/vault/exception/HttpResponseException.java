package bg.sofia.uni.fmi.mjt.vault.exception;

public class HttpResponseException extends RuntimeException {
    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(String message, Exception e) {
        super(message, e);
    }
}
