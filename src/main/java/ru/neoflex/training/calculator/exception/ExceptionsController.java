package ru.neoflex.training.calculator.exception;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleValidationError(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = new ArrayList<>(ex.getFieldErrors());
        fieldErrors.sort(Comparator.comparing(FieldError::getField));
        FieldError fieldError = fieldErrors.get(0);
        return new ResponseEntity<>(
                new Error(fieldError.getDefaultMessage(),
                        OffsetDateTime.now(),
                        400,
                        "Bad request"),
                HttpStatus.BAD_REQUEST);
    }
}
