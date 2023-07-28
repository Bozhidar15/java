package bg.sofia.uni.fmi.mjt.vault.exception;

public class HashCanNotBeCreatedException extends RuntimeException {
    public HashCanNotBeCreatedException(String message) {
        super(message);
    }

    public HashCanNotBeCreatedException(String message, Exception e) {
        super(message, e);
    }
}
