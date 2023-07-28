package bg.sofia.uni.fmi.mjt.vault.exception;

public class EncryptDecryptException extends RuntimeException {
    public EncryptDecryptException(String message) {
        super(message);
    }

    public EncryptDecryptException(String message, Exception e) {
        super(message, e);
    }
}
