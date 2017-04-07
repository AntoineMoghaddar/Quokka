package Design;

/**
 * Created by Antoine Moghaddar on 31-1-2016.
 */
public class LineException extends Exception {

    private String message;

    public LineException(String message) {
        this.message = message;
    }

    public LineException(String message, String message1) {
        super(message);
        message = message1;
    }

    public LineException(String message, Throwable cause, String message1) {
        super(message, cause);
        message = message1;
    }

    public LineException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    public LineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        message = message1;
    }
}
