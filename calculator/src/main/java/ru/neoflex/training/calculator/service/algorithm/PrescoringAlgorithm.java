package ru.neoflex.training.calculator.service.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.OfferSale;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;

public abstract class PrescoringAlgorithm {
    //  название алгоритма
    protected static String AlgoName = "default";
    //  offer sale, который будет применять к данному алгоритму
    protected OfferSale offerSale;
    //  точность расчетов
    protected final MathContext mcCount = new MathContext(50);

    private BigDecimal baseRate;

    protected final OfferSale defaultOfferSale = new OfferSale(BigDecimal.valueOf(0),
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(1));

    public PrescoringAlgorithm(OfferSale offerSale, BigDecimal baseRate) {
        if (offerSale == null) {
            offerSale = defaultOfferSale;
        }
        this.offerSale = offerSale;
        this.baseRate = baseRate;
    }

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

    protected UUID getStatementId() {
        return UUID.randomUUID();
    }

    protected BigDecimal calculateAmount(LoanStatementRequestDto loanStatement) {
        return loanStatement.getAmount().multiply(offerSale.getAmountMultiply());
    }

    protected int calculateTerm(LoanStatementRequestDto loanStatement) {
        return BigDecimal.valueOf(loanStatement.getTerm()).multiply(offerSale.getTempMultiply()).intValue();
    }

    public BigDecimal calculateRate(LoanStatementRequestDto loanStatement) {
        return baseRate.subtract(offerSale.getRateDecrease().round(mcCount));
    }

    public static String getName() {
        return AlgoName;
    }
}
