package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class RuleAlreadyDefinedException extends RuntimeException {
    public RuleAlreadyDefinedException(String mess) {
        super(mess);
    }
    public  RuleAlreadyDefinedException(String mess, Throwable cause) {
        super(mess, cause);
    }
}