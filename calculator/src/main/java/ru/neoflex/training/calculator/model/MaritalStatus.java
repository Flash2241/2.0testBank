package ru.neoflex.training.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Брачный статус")
public enum MaritalStatus {
    @Schema(description = "один")
    SINGLE,
    @Schema(description = "в браке")
    MARRIED,
    @Schema(description = "разведен")
    DIVORCED,
    @Schema(description = "вдова")
    WIDOWED
}
