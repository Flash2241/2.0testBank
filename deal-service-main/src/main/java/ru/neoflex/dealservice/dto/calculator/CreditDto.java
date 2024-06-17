package ru.neoflex.dealservice.dto.calculator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
