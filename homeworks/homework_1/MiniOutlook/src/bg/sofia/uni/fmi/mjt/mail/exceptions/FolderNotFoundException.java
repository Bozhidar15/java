package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String mess) {
        super(mess);
    }
    public  FolderNotFoundException(String mess, Throwable cause) {
        super(mess, cause);
    }
}