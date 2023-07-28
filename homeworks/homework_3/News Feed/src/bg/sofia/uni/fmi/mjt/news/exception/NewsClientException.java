package bg.sofia.uni.fmi.mjt.news.exception;

public class NewsClientException extends RuntimeException {
    public NewsClientException(String message) {
        super(message);
    }

    public NewsClientException(String message, Exception e) {
        super(message, e);
    }
}
