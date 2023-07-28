package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class TeamNotFoundException extends RoomAlreadyExistsException{
    public TeamNotFoundException() {
        super("Team not found!");
    }
}
