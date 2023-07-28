package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class FridgeCapacityExceededException extends RuntimeException {
    public FridgeCapacityExceededException(String mess) {
        super(mess);
    }
    public FridgeCapacityExceededException(String mess, Throwable cause) {

        super(mess, cause);
    }
}
