package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String mess) {
        super(mess);
    }
    public InsufficientQuantityException(String mess, Throwable cause) {
        super(mess, cause);
    }
}
