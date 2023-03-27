package peaksoft.excetions;

public class ExistsException extends RuntimeException {
    public ExistsException() {
        super();
    }

    public ExistsException(String message) {
        super(message);
    }
}
