package murkeev.currencyexchangerapi.exceptions;

public class IncorrectLoginOrPasswordException extends RuntimeException {
    public IncorrectLoginOrPasswordException(String message) {
        super(message);
    }
}
