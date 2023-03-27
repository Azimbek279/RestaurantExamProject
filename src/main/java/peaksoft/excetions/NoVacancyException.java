package peaksoft.excetions;

public class NoVacancyException extends RuntimeException {
    public NoVacancyException() {
        super();
    }

    public NoVacancyException(String message) {
        super(message);
    }
}
