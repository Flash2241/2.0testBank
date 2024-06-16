package ru.neoflex.training.calculator.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CreditDeniedException extends RuntimeException {

    private String reason;

    public CreditDeniedException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
