package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomAlreadyExistsException extends Exception{
    public RoomAlreadyExistsException(String mess) {
        super(mess);
    }

    public RoomAlreadyExistsException(String mess, Throwable cause) {
        super(mess, cause);
    }
}
