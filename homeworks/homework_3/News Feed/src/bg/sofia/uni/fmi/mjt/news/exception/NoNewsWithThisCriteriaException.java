package bg.sofia.uni.fmi.mjt.news.exception;

public class NoNewsWithThisCriteriaException extends NewsClientException {
    public NoNewsWithThisCriteriaException(String message) {
        super(message);
    }
}
