package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String mess) {
        super(mess);
    }
    public AccountAlreadyExistsException(String mess, Throwable cause) {
        super(mess, cause);
    }
}
