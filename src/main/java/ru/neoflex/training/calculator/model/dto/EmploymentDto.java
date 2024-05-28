package ru.neoflex.training.calculator.model.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.training.calculator.model.EmploymentPosition;
import ru.neoflex.training.calculator.model.EmploymentStatus;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDto {
    @NotNull(message = "'employmentStatus' field is required")
    private EmploymentStatus employmentStatus;
    @NotNull(message = "'employerINN' field is required")
    private String employerINN;
    @NotNull(message = "'salary' field is required")
    private BigDecimal salary;
    @NotNull(message = "'position' field is required")
    private EmploymentPosition position;
    @NotNull(message = "'workExperienceTotal' field is required")
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
