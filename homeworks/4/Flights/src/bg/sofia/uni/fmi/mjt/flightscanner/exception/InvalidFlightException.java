package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class InvalidFlightException extends RuntimeException {
    public InvalidFlightException(String mess) {
        super(mess);
    }
    public InvalidFlightException(String mess, Throwable cause) {
        super(mess, cause);
    }
}