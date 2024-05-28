package ru.neoflex.training.calculator.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.training.calculator.model.Gender;
import ru.neoflex.training.calculator.model.MaritalStatus;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDto {
    @NotNull(message = "'amount' field is required")
    @Min(value = 30000, message = "'amount' must be greater than or equal to 30000")
    private BigDecimal amount;
    @NotNull(message = "'term' field is required")
    @Min(value = 6, message = "'term' must be greater than or equal to six")
    private Integer term;
    @NotNull(message = "'firstName' field is required")
    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "'firstName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'firstName' length must be in range from 2 to 30")
    private String firstName;
    @NotNull(message = "'lastName' field is required")
    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "'lastName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'lastName' length must be in range from 2 to 30")
    private String lastName;
    @Pattern(regexp = "^[a-zA-Z]*$",
            message = "'middleName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'middleName' length must be in range from 2 to 30")
    private String middleName;
    @NotNull(message = "'gender' field is required")
    private Gender gender;
    @NotNull(message = "'birthdate' field is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate birthdate;
    @NotNull(message = "'passportSeries' field is required")
    @Pattern(regexp = "^[0-9]{4}$",
            message = "'passportSeries' must contains four numbers")
    private String passportSeries;
    @NotNull(message = "'passportNumber' field is required")
    @Pattern(regexp = "^[0-9]{6}$",
            message = "'passportNumber' must contains six numbers")
    private String passportNumber;
    @NotNull(message = "'passportIssueDate' field is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate passportIssueDate;
    @NotNull(message = "'passportIssueBranch' field is required")
    @Size(min = 5, message = "'passportIssueBranch' field must be comprehensive")
    private String passportIssueBranch;
    @NotNull(message = "'maritalStatus' field is required")
    private MaritalStatus maritalStatus;
    @NotNull(message = "'dependentAmount' field is required")
    @Min(value = 0)
    private Integer dependentAmount;
    @Valid
    @NotNull(message = "'employment' field is required")
    private EmploymentDto employment;
    @NotNull(message = "'accountNumber' field is required")
    @Pattern(regexp = "^[0-9]{10}", message = "'accountNumber' contains 10 digits")
    private String accountNumber;
    @NotNull(message = "'isInsuranceEnabled' field is required")
    private Boolean isInsuranceEnabled;
    @NotNull(message = "'isSalaryClient' field is required")
    private Boolean isSalaryClient;

}
