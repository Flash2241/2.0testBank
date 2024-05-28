package ru.neoflex.training.calculator.model.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.neoflex.training.calculator.model.EmploymentPosition;
import ru.neoflex.training.calculator.model.EmploymentStatus;

@Getter
@Setter
@Builder
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
