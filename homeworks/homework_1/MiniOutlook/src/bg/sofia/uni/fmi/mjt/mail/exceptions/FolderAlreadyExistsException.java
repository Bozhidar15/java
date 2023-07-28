package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class FolderAlreadyExistsException extends RuntimeException {
    public FolderAlreadyExistsException(String mess) {
        super(mess);
    }
    public  FolderAlreadyExistsException(String mess, Throwable cause) {
        super(mess, cause);
    }
}