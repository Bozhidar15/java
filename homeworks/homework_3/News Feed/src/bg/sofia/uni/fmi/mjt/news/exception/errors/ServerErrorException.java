package bg.sofia.uni.fmi.mjt.news.exception.errors;

public class ServerErrorException extends Exception {
    public ServerErrorException(String message) {
        super(message);
    }
    public ServerErrorException(String message, Exception e) {
        super(message, e);
    }
}
