package ru.neoflex.dealservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "LoanOffer", description = "Кредитное предложение прескоринга")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDto {
    @Schema(description = "id предложения")
    private UUID statementId;
    @Schema(description = "запрошенное количество средств", example = "123.123")
    private BigDecimal requestedAmount;
    @Schema(description = "доступное количество средств", example = "123.123")
    private BigDecimal totalAmount;
    @Schema(description = "доступный срок", example = "123")
    private Integer term;
    @Schema(description = "процентная ставка", example = "12.123")
    private BigDecimal rate;
    @Schema(description = "застрахован ли кредит", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "нужна ли зарплатная карта", example = "true")
    private Boolean isSalaryClient;
}
