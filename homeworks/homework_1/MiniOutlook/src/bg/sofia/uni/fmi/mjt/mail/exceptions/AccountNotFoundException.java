package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String mess) {
        super(mess);
    }
    public AccountNotFoundException(String mess, Throwable cause) {
        super(mess, cause);
    }
}