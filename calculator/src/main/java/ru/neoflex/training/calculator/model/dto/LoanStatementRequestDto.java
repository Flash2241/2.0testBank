package ru.neoflex.training.calculator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "LoanStatement", description = "Данные для прескоринга")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoanStatementRequestDto {
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
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$",
            message = "'firstName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'firstName' length must be in range from 2 to 30")
    private String firstName;
    @Schema(description = "фамилия кредитора", example = "Воронин")
    @NotNull(message = "'lastName' field is required")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$",
            message = "'lastName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'lastName' length must be in range from 2 to 30")
    private String lastName;
    @Schema(description = "второе имя кредитора", example = "Сан")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$",
            message = "'middleName' can contain only latin letters")
    @Size(min = 2, max = 30, message = "'middleName' length must be in range from 2 to 30")
    private String middleName;
    @Schema(description = "почта кредитора", example = "example@example.com")
    @NotNull(message = "'email' field is required")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "'email' must be valid email address")
    private String email;
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
}
