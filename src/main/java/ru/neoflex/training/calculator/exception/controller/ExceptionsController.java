package ru.neoflex.training.calculator.exception.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.neoflex.training.calculator.exception.CreditDeniedException;

@Slf4j
@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleValidationError(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = new ArrayList<>(ex.getFieldErrors());
        fieldErrors.sort(Comparator.comparing(FieldError::getField));
        FieldError fieldError = fieldErrors.get(0);
        log.info("Bad request, message: " + fieldError.getDefaultMessage());
        return new ResponseEntity<>(
                new Error(fieldError.getDefaultMessage(),
                        OffsetDateTime.now(),
                        400,
                        "Bad request"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<Error> handleValidationError(InvalidFormatException ex) {
        String fieldName = ex.getPath().get(0).getFieldName();
        if (ex.getTargetType() == LocalDate.class) {
            String message = "'" + fieldName + "' illegal value: " + ex.getValue();
            log.info("Bad request, message: " + message);
            return new ResponseEntity<>(
                    new Error(message,
                            OffsetDateTime.now(),
                            400,
                            "Bad request"),
                    HttpStatus.BAD_REQUEST);
        }
        String message = "'" + fieldName + "' illegal value";
        log.info("Bad request, message: " + message);
        return new ResponseEntity<>(
                new Error(message,
                        OffsetDateTime.now(),
                        400,
                        "Bad request"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CreditDeniedException.class})
    public ResponseEntity<Object> denied(CreditDeniedException ex) {
        log.info("User scoring was denied, reason: " + ex.getReason());
        return new ResponseEntity<>(Map.of("status", "denied", "reason", ex.getReason()),
                HttpStatus.OK);
    }
}
