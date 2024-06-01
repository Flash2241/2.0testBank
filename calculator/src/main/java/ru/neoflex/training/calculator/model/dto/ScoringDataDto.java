package ru.neoflex.training.calculator.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(name = "ScoringData", description = "Данные для расчета кредита")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ScoringDataDto {
    @Schema(description = "сумма кредита", example = "200000")
    @NotNull(message = "'amount' field is required")
    @Min(value = 30000, message = "'amount' must be greater than or equal to 30000")
    private BigDecimal amount;
    @Schema(description = "продолжительность кредита в месяцах", example = "18")
    @NotNull(message = "'term' field is required")
    @Min(value = 6, message = "'term' must be greater than or equal to six")
    private Integer term;
    @Schema(description = "имя кредитора", example = "Дмитрий")
    @NotNull(message = "'firstName' field is required")
    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "'firstName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'firstName' length must be in range from 2 to 30")
    private String firstName;
    @Schema(description = "фамилия кредитора", example = "Воронин")
    @NotNull(message = "'lastName' field is required")
    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "'lastName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'lastName' length must be in range from 2 to 30")
    private String lastName;
    @Schema(description = "второе имя кредитора", example = "Сан")
    @Pattern(regexp = "^[a-zA-Z]*$",
            message = "'middleName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'middleName' length must be in range from 2 to 30")
    private String middleName;
    @Schema(description = "пол кредитора", example = "MALE")
    @NotNull(message = "'gender' field is required")
    private Gender gender;
    @Schema(description = "дата рождения кредитора", example = "2000.01.01")
    @NotNull(message = "'birthdate' field is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate birthdate;
    @Schema(description = "серия паспорта кредитора", example = "4567")
    @NotNull(message = "'passportSeries' field is required")
    @Pattern(regexp = "^[0-9]{4}$",
            message = "'passportSeries' must contains four numbers")
    private String passportSeries;
    @Schema(description = "номер паспорта кредитора", example = "456712")
    @NotNull(message = "'passportNumber' field is required")
    @Pattern(regexp = "^[0-9]{6}$",
            message = "'passportNumber' must contains six numbers")
    private String passportNumber;
    @Schema(description = "дата выдачи паспорта кредитора", example = "2021.02.13")
    @NotNull(message = "'passportIssueDate' field is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate passportIssueDate;
    @Schema(description = "место выдачи паспорта кредитора", example = "ГУ МВД Москвы")
    @NotNull(message = "'passportIssueBranch' field is required")
    @Size(min = 5, message = "'passportIssueBranch' field must be comprehensive")
    private String passportIssueBranch;
    @Schema(description = "брачное состояние кредитора", example = "DIVORCED")
    @NotNull(message = "'maritalStatus' field is required")
    private MaritalStatus maritalStatus;
    @Schema(description = "количество детей кредитора", example = "2")
    @NotNull(message = "'dependentAmount' field is required")
    @Min(value = 0)
    private Integer dependentAmount;
    @Schema(description = "описание работы кредитора")
    @Valid
    @NotNull(message = "'employment' field is required")
    private EmploymentDto employment;
    @Schema(description = "номер аккаунта кредитора", example = "9996662221")
    @NotNull(message = "'accountNumber' field is required")
    @Pattern(regexp = "^[0-9]{10}", message = "'accountNumber' contains 10 digits")
    private String accountNumber;
    @Schema(description = "включена ли страховка кредита", example = "true")
    @NotNull(message = "'isInsuranceEnabled' field is required")
    private Boolean isInsuranceEnabled;
    @Schema(description = "есть ли зарплатная карта", example = "true")
    @NotNull(message = "'isSalaryClient' field is required")
    private Boolean isSalaryClient;

}
