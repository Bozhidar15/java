package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class PlatformCapacityExceededException extends RuntimeException {
    public PlatformCapacityExceededException(String mess){
        super(mess);
    }
    public PlatformCapacityExceededException(String mess,Throwable cause){
        super(mess,cause);
    }
}
