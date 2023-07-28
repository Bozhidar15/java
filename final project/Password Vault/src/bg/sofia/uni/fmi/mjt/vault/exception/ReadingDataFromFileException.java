package bg.sofia.uni.fmi.mjt.vault.exception;

public class ReadingDataFromFileException extends RuntimeException {
    public ReadingDataFromFileException(String message) {
        super(message);
    }

    public ReadingDataFromFileException(String message, Exception e) {
        super(message, e);
    }
}
