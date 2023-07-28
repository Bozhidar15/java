package bg.sofia.uni.fmi.mjt.vault.exception;

public class PasswordIsNotInTheDataBaseExseption extends RuntimeException {
    public PasswordIsNotInTheDataBaseExseption(String message) {
        super(message);
    }

    public PasswordIsNotInTheDataBaseExseption(String message, Exception e) {
        super(message, e);
    }
}
