package bg.sofia.uni.fmi.mjt.vault.exception;

public class UsernameIsAlreadyTakenException extends RuntimeException {
    public UsernameIsAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameIsAlreadyTakenException(String message, Exception e) {
        super(message, e);
    }
}
