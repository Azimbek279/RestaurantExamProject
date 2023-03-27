package peaksoft.excetions;

public class NoValidException extends RuntimeException {
    public NoValidException() {
        super();
    }

    public NoValidException(String message) {
        super(message);
    }
}
