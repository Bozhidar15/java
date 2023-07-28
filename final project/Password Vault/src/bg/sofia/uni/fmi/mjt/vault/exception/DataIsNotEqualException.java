package bg.sofia.uni.fmi.mjt.vault.exception;

public class DataIsNotEqualException extends RuntimeException {
    public DataIsNotEqualException(String message) {
        super(message);
    }

    public DataIsNotEqualException(String message, Exception e) {
        super(message, e);
    }
}
