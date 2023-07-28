package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException() {
        super("the platform does not contain an escape room with the specified name");
    }
}
