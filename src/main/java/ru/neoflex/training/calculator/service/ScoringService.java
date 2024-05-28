package ru.neoflex.training.calculator.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.OfferSale;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;

@RequiredArgsConstructor
@Service
public class ScoringService {

    @Value("${scoring.offers.base-rate}")
    private Integer rate;

    private final ScoringConfiguration scoringConfiguration;

    private final OfferSale defaultOfferSale = new OfferSale(1,
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(1));

    public List<LoanOfferDto> calculatePrescoring(LoanStatementRequestDto request) {
        OfferSale salarySale = scoringConfiguration.getSales().computeIfAbsent("salary", (x) -> defaultOfferSale);
        OfferSale insuranceSale = scoringConfiguration.getSales().computeIfAbsent("insurance", (x) -> defaultOfferSale);
        OfferSale insuranceSalarySale = scoringConfiguration.getSales().computeIfAbsent("insurance-salary", (x) -> defaultOfferSale);
        return List.of(
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount())
                        .totalAmount(request.getAmount())
                        .term(request.getTerm())
                        .rate(BigDecimal.valueOf((rate + 100D) / 100))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(insuranceSale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(insuranceSale.getTempMultiply()).intValue())
                        .rate(BigDecimal.valueOf(((rate - insuranceSale.getRatePointsDecrease()) + 100D) / 100))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(salarySale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(salarySale.getTempMultiply()).intValue())
                        .rate(BigDecimal.valueOf(((rate - salarySale.getRatePointsDecrease()) + 100D) / 100))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(insuranceSalarySale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(insuranceSalarySale.getTempMultiply()).intValue())
                        .rate(BigDecimal.valueOf(((rate - insuranceSalarySale.getRatePointsDecrease()) + 100D) / 100))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .build()
        );
    }

    public void calculateScoring() {

    }
}
