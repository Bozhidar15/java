package bg.sofia.uni.fmi.mjt.vault.exception;

public class CompromisedException extends RuntimeException {
    public CompromisedException(String message) {
        super(message);
    }

    public CompromisedException(String message, Exception e) {
        super(message, e);
    }
}
