package ru.neoflex.training.calculator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.training.calculator.model.EmploymentPosition;
import ru.neoflex.training.calculator.model.EmploymentStatus;

@Schema(name = "Employment", description = "Информация о трудоустройстве кредитора")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmploymentDto {
    @Schema(description = "занятость кредитора", example = "SELF_EMPLOYED")
    @NotNull(message = "'employmentStatus' field is required")
    private EmploymentStatus employmentStatus;
    @Schema(description = "инн кредитора", example = "3664069397")
    @NotNull(message = "'employerINN' field is required")
    @Pattern(regexp = "^[0-9]{10}", message = "'employerINN' contains 10 digits")
    private String employerINN;
    @Schema(description = "зарплата кредитора", example = "123.123")
    @NotNull(message = "'salary' field is required")
    private BigDecimal salary;
    @Schema(description = "уровень позиции кредитора на работе", example = "LOW")
    @NotNull(message = "'position' field is required")
    private EmploymentPosition position;
    @Schema(description = "трудовой стаж всего в месяцах", example = "7")
    @NotNull(message = "'workExperienceTotal' field is required")
    @Min(value = 0, message = "'workExperienceTotal' cannot be lower than zero")
    private Integer workExperienceTotal;
    @Schema(description = "трудовой стаж на текущей работе в месяцах", example = "5")
    @NotNull(message = "'workExperienceCurrent' field is required")
    @Min(value = 0, message = "'workExperienceCurrent' cannot be lower than zero")
    private Integer workExperienceCurrent;
}
