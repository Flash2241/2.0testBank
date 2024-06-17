package ru.neoflex.training.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "трудовой статус")
public enum EmploymentStatus {
    @Schema(description = "безработный")
    UNEMPLOYED,
    @Schema(description = "работает на себя")
    SELF_EMPLOYED,
    @Schema(description = "трудоустроен")
    EMPLOYED,
    @Schema(description = "владелец бизнеса")
    BUSINESS_OWNER
}
