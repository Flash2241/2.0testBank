package ru.neoflex.dealservice.dto.calculator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "PaymentScheduleElement", description = "Описание оплаты кредита за период")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElementDto {
    @Schema(description = "порядковый номер оплаты", example = "3")
    private Integer number;
    @Schema(description = "дата оплаты", example = "2024-06-30")
    private LocalDate date;
    @Schema(description = "всего выплачено", example = "1234.1234")
    private BigDecimal totalPayment;
    @Schema(description = "оплата комиссии", example = "1234.1234")
    private BigDecimal interestPayment;
    @Schema(description = "оплата тела кредита", example = "1234.1234")
    private BigDecimal debtPayment;
    @Schema(description = "оставшееся тело кредита", example = "1234.1234")
    private BigDecimal remainingDebt;
}
