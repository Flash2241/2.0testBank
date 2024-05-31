package ru.neoflex.training.calculator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Credit", description = "Рассчитанный кредит")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditDto {
    @Schema(description = "сумма кредита", example = "200000")
    private BigDecimal amount;
    @Schema(description = "продолжительность кредита в месяцах", example = "18")
    private Integer term;
    @Schema(description = "ежемесячная плата", example = "1000.1234")
    private BigDecimal monthlyPayment;
    @Schema(description = "процентная ставка", example = "15.164")
    private BigDecimal rate;
    @Schema(description = "полная стоимость кредита", example = "2222.1234")
    private BigDecimal psk;
    @Schema(description = "включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "есть ли зарплатная карта", example = "false")
    private Boolean isSalaryClient;
    @Schema(description = "ежемесячные выплаты подробно")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
