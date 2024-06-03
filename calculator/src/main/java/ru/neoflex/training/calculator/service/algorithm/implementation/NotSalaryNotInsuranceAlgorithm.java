package ru.neoflex.training.calculator.service.algorithm.implementation;

import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.service.algorithm.PrescoringAlgorithm;

public class NotSalaryNotInsuranceAlgorithm extends PrescoringAlgorithm {
    protected static String AlgoName = "no";

    public NotSalaryNotInsuranceAlgorithm(ScoringConfiguration scoringConfiguration) {
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
                .isSalaryClient(false)
                .build();
    }

    public static String getName() {
        return AlgoName;
    }
}
