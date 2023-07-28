package bg.sofia.uni.fmi.mjt.news.exception.errors;


public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Exception e) {
        super(message, e);
    }
}
