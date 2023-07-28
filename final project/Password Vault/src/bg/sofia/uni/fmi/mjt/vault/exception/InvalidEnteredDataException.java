package bg.sofia.uni.fmi.mjt.vault.exception;

public class InvalidEnteredDataException extends RuntimeException {
    public InvalidEnteredDataException(String message) {
        super(message);
    }

    public InvalidEnteredDataException(String message, Exception e) {
        super(message, e);
    }
}
