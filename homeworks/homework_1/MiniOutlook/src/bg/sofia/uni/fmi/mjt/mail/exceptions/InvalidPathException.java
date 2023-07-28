package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException(String mess) {
        super(mess);
    }
    public  InvalidPathException(String mess, Throwable cause) {
        super(mess, cause);
    }
}