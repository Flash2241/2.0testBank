package ru.neoflex.training.calculator.service.algorithm.implementation;

import org.springframework.stereotype.Service;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.service.algorithm.PrescoringAlgorithm;

@Service
public class SalaryAlgorithm extends PrescoringAlgorithm {
    protected static String AlgoName = "salary";

    public SalaryAlgorithm(ScoringConfiguration scoringConfiguration) {
        super(scoringConfiguration.getSales().computeIfAbsent(AlgoName, (x) -> null), scoringConfiguration.getBaseRate());
    }

    @Override
    public LoanOfferDto calculatePrescoring(LoanStatementRequestDto loanStatement) {
        return LoanOfferDto.builder()
                .statementId(getStatementId())
                .requestedAmount(loanStatement.getAmount())
                .totalAmount(calculateAmount(loanStatement))
                .term(calculateTerm(loanStatement))
                .rate(calculateRate(loanStatement))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();
    }

    public static String getName() {
        return AlgoName;
    }
}
