package pl.kania.etd;

public class CriticalException extends RuntimeException {

    public CriticalException(String message) {
        super("Fatal app error occured. Reason: " + message);
    }
}
