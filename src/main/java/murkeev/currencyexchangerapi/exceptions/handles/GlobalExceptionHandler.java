package murkeev.currencyexchangerapi.exceptions.handles;

import murkeev.currencyexchangerapi.exceptions.CurrencyNotFoundException;
import murkeev.currencyexchangerapi.exceptions.SaveException;
import murkeev.currencyexchangerapi.exceptions.UpdateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
