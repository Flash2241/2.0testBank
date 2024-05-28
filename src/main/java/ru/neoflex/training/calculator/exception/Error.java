package ru.neoflex.training.calculator.exception;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Error {
    private String message;
    private OffsetDateTime date;
    private Integer status;
    private String error;
}
