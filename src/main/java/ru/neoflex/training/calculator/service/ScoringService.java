package ru.neoflex.training.calculator.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.exception.CreditDeniedException;
import ru.neoflex.training.calculator.model.EmploymentStatus;
import ru.neoflex.training.calculator.model.Gender;
import ru.neoflex.training.calculator.model.OfferSale;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.model.dto.PaymentScheduleElementDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScoringService {

    private MathContext mcCount = new MathContext(50);
    private int scalePrint = 5;

    @Value("${scoring.offers.base-rate}")
    private BigDecimal baseRate;

    @Value("${scoring.offers.insurance-price}")
    private BigDecimal insurancePrice;

    private final ScoringConfiguration scoringConfiguration;

    private final OfferSale defaultOfferSale = new OfferSale(BigDecimal.valueOf(0),
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(1));

    public List<LoanOfferDto> calculatePrescoring(LoanStatementRequestDto request) {
        validatePrescoring(request);
        OfferSale salarySale = scoringConfiguration.getSales().computeIfAbsent("salary", (x) -> defaultOfferSale);
        OfferSale insuranceSale = scoringConfiguration.getSales().computeIfAbsent("insurance", (x) -> defaultOfferSale);
        OfferSale insuranceSalarySale = scoringConfiguration.getSales().computeIfAbsent("insurance-salary", (x) -> defaultOfferSale);
        log.info("Create offers to client {} {}", request.getFirstName(), request.getLastName());
        return List.of(
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount())
                        .totalAmount(request.getAmount())
                        .term(request.getTerm())
                        .rate(baseRate.round(mcCount))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(insuranceSale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(insuranceSale.getTempMultiply()).intValue())
                        .rate(baseRate.subtract(insuranceSale.getRateDecrease()).round(mcCount))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(salarySale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(salarySale.getTempMultiply()).intValue())
                        .rate(baseRate.subtract(insuranceSale.getRateDecrease()).round(mcCount))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(request.getAmount().multiply(insuranceSalarySale.getAmountMultiply()))
                        .totalAmount(request.getAmount())
                        .term(BigDecimal.valueOf(request.getTerm()).multiply(insuranceSalarySale.getTempMultiply()).intValue())
                        .rate(baseRate.subtract(insuranceSalarySale.getRateDecrease().round(mcCount)))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .build()
        );
    }

    private void validatePrescoring(LoanStatementRequestDto requestDto) {
        log.info("Checking prescoring person data is he possible to get credit");
        Period age = Period.between(requestDto.getBirthdate(), LocalDate.now());
        if (age.getYears() < 18) {
            throw new CreditDeniedException("denied", "bad age");
        }
    }

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        validateScoring(scoringData);
        BigDecimal rate = calculateRate(baseRate, scoringData).divide(BigDecimal.valueOf(100), mcCount);
        BigDecimal monthlyPayment = calculateMonthlyPayment(scoringData.getAmount(), rate, scoringData.getTerm());

        return CreditDto.builder()
                .amount(scoringData.getAmount().setScale(scalePrint, RoundingMode.HALF_UP))
                .term(scoringData.getTerm())
                .rate(rate.multiply(BigDecimal.valueOf(100)).setScale(scalePrint, RoundingMode.HALF_UP))
                .monthlyPayment(monthlyPayment.setScale(scalePrint, RoundingMode.HALF_UP))
                .psk(calculatePsk(scoringData.getAmount(), monthlyPayment, scoringData.getTerm(), scoringData.getIsInsuranceEnabled()).setScale(scalePrint, RoundingMode.HALF_UP))
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .paymentSchedule(calculatePaymentSchedule(scoringData.getAmount(), rate, scoringData.getTerm(), monthlyPayment))
                .build();
    }

    private void validateScoring(ScoringDataDto scoringData) {
        log.info("Checking scoring person data is he possible to get credit");
        if (EmploymentStatus.UNEMPLOYED.equals(scoringData.getEmployment().getEmploymentStatus())) {
            throw new CreditDeniedException("denied", "unemployed");
        }
        if (!scoringData.getEmployment().getSalary().equals(BigDecimal.valueOf(0))
            && scoringData.getAmount().divide(scoringData.getEmployment().getSalary(), mcCount).compareTo(BigDecimal.valueOf(25)) >= 0) {
            throw new CreditDeniedException("denied", "amount is 25 times greater than salary");
        }
        if (scoringData.getEmployment().getSalary().equals(BigDecimal.valueOf(0))) {
            throw new CreditDeniedException("denied", "salary is zero");
        }
        Period age = Period.between(scoringData.getBirthdate(), LocalDate.now());
        if (age.getYears() < 20 || age.getYears() > 65) {
            throw new CreditDeniedException("denied", "bad age");
        }
        if (scoringData.getEmployment().getWorkExperienceTotal() < 18) {
            throw new CreditDeniedException("denied", "not enough total work experience");
        }
        if (scoringData.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new CreditDeniedException("denied", "not enough current work experience");
        }
    }

    private BigDecimal calculateRate(BigDecimal baseRate, ScoringDataDto scoringData) {
        switch (scoringData.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED -> baseRate = baseRate.add(BigDecimal.valueOf(1));
            case BUSINESS_OWNER -> baseRate = baseRate.add(BigDecimal.valueOf(2));
        }
        switch (scoringData.getEmployment().getPosition()) {
            case MEDIUM -> baseRate = baseRate.subtract(BigDecimal.valueOf(2));
            case HIGH -> baseRate = baseRate.subtract(BigDecimal.valueOf(3));
        }
        switch (scoringData.getMaritalStatus()) {
            case MARRIED -> baseRate = baseRate.subtract(BigDecimal.valueOf(3));
            case DIVORCED -> baseRate = baseRate.add(BigDecimal.valueOf(1));
        }
        int years = Period.between(scoringData.getBirthdate(), LocalDate.now()).getYears();
        if (Gender.MALE.equals(scoringData.getGender())
                && (years >= 30 && years <= 55)) {
            baseRate = baseRate.subtract(BigDecimal.valueOf(3));
        }
        if (Gender.FEMALE.equals(scoringData.getGender())
                && (years >= 32 && years <= 60)) {
            baseRate = baseRate.subtract(BigDecimal.valueOf(3));
        }
        if (Gender.NON_BINARY.equals(scoringData.getGender())) {
            baseRate = baseRate.add(BigDecimal.valueOf(7));
        }
        if (scoringData.getIsSalaryClient() && scoringData.getIsInsuranceEnabled()) {
            OfferSale insuranceSalarySale = scoringConfiguration.getSales().computeIfAbsent("insurance-salary", (x) -> defaultOfferSale);
            baseRate = baseRate.subtract(insuranceSalarySale.getRateDecrease());
        } else if (scoringData.getIsInsuranceEnabled()) {
            OfferSale insuranceSale = scoringConfiguration.getSales().computeIfAbsent("insurance", (x) -> defaultOfferSale);
            baseRate = baseRate.subtract(insuranceSale.getRateDecrease());
        } else if (scoringData.getIsSalaryClient()) {
            OfferSale salarySale = scoringConfiguration.getSales().computeIfAbsent("salary", (x) -> defaultOfferSale);
            baseRate = baseRate.subtract(salarySale.getRateDecrease());
        }
        if (baseRate.compareTo(BigDecimal.valueOf(1)) < 0) {
            baseRate = BigDecimal.valueOf(1);
        }
        return baseRate;
    }

    /**
     * расчет по формуле:
     * a * b * (1 + b)^c / ((1 + b) ^ c - 1)
     * a - сумма кредита,
     * b - месячная процентная ставка (считается как годовая ставка / 12)
     * c - количество платежей
     *
     * @param amount сумма под кредит
     * @param rate годовая ставка
     * @param paymentsNumber количество выплат
     * @return ежемесячный платеж
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int paymentsNumber) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), mcCount);
        BigDecimal precalculatedTemp = monthlyRate.add(BigDecimal.valueOf(1)).pow(paymentsNumber);
        BigDecimal divisible = monthlyRate.multiply(precalculatedTemp);
        BigDecimal divider = precalculatedTemp.subtract(BigDecimal.valueOf(1));
        return amount.multiply(divisible.divide(divider, mcCount));
    }

    /**
     *
     * @param amount
     * @param monthlyPayment
     * @param term
     * @return
     */
    private BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, int term, boolean isInsuranceEnabled) {
        //   считаем все выплаты
        BigDecimal payment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        //   считаем ежегодную страховку
        if (isInsuranceEnabled) {
            payment = payment.add(insurancePrice.multiply(BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), mcCount)));
        }
        return payment;
    }

    private List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount,
                                                                     BigDecimal rate,
                                                                     int term,
                                                                     BigDecimal monthlyPayment) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), mcCount);
        BigDecimal debtPayment = monthlyPayment.divide(monthlyRate.add(BigDecimal.valueOf(1)), mcCount);
        BigDecimal interestPayment = monthlyPayment.subtract(debtPayment);
        BigDecimal totalPayment = BigDecimal.valueOf(0);
        BigDecimal debtPaymentTotal = BigDecimal.valueOf(0);
        List<PaymentScheduleElementDto> payments = new ArrayList<>();
        for (int i = 0; i < term; ++i) {
            interestPayment = amount.subtract(debtPaymentTotal).multiply(monthlyRate);
            debtPayment = monthlyPayment.subtract(interestPayment);
            debtPaymentTotal = debtPaymentTotal.add(debtPayment);
            totalPayment = totalPayment.add(monthlyPayment);
            payments.add(
                    PaymentScheduleElementDto.builder()
                            .number(i + 1)
                            .date(LocalDate.now().plusMonths(i + 1))
                            .totalPayment(totalPayment.setScale(scalePrint, RoundingMode.HALF_UP))
                            .interestPayment(interestPayment.setScale(scalePrint, RoundingMode.HALF_UP))
                            .debtPayment(debtPayment.setScale(scalePrint, RoundingMode.HALF_UP))
                            .remainingDebt(amount.subtract(debtPaymentTotal).setScale(scalePrint, RoundingMode.HALF_UP))
                            .build()
            );
        }
        return payments;
    }
}
