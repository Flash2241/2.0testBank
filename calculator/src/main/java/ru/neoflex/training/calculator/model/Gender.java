package ru.neoflex.training.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "пол человека")
public enum Gender {
    @Schema(description = "мужчина")
    MALE,
    @Schema(description = "женщина")
    FEMALE,
    @Schema(description = "не определился")
    NON_BINARY
}
