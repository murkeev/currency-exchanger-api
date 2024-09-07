package murkeev.currencyexchangerapi.exceptions.handles;

import murkeev.currencyexchangerapi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFoundException(CurrencyNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(SaveException.class)
    public ResponseEntity<ErrorResponse> handleSaveException(SaveException e) {
        return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<ErrorResponse> handleUpdateException(UpdateException e) {
        return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(IncorrectLoginOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectLoginOrPasswordException(IncorrectLoginOrPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage(), LocalDateTime.now()));
    }
}
