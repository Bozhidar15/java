package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class FlightCapacityExceededException extends RuntimeException {
    public FlightCapacityExceededException(String mess) {

        super(mess);
    }

    public FlightCapacityExceededException(String mess, Throwable cause) {

        super(mess, cause);
    }
}
