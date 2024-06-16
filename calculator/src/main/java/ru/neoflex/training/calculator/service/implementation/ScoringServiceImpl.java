package ru.neoflex.training.calculator.service.implementation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.exception.CreditDeniedException;
import ru.neoflex.training.calculator.exception.ErrorMessages;
import ru.neoflex.training.calculator.model.EmploymentStatus;
import ru.neoflex.training.calculator.model.Gender;
import ru.neoflex.training.calculator.model.OfferSale;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.model.dto.PaymentScheduleElementDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.service.ScoringService;
import ru.neoflex.training.calculator.service.algorithm.PrescoringAlgorithm;
import ru.neoflex.training.calculator.service.algorithm.implementation.InsuranceAlgorithm;
import ru.neoflex.training.calculator.service.algorithm.implementation.NotSalaryNotInsuranceAlgorithm;
import ru.neoflex.training.calculator.service.algorithm.implementation.SalaryAlgorithm;
import ru.neoflex.training.calculator.service.algorithm.implementation.SalaryInsuranceAlgorithm;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {
    //  точность расчетов
    private final MathContext mcCount = new MathContext(50);
    //  количество знаков для округления при отправке ответа
    private final int scalePrint = 5;

    @Value("${scoring.offers.base-rate}")
    private BigDecimal baseRate;

    @Value("${scoring.offers.insurance-price}")
    private BigDecimal insurancePrice;   // ежегодно нужно оплачивать

    private final List<PrescoringAlgorithm> prescoringAlgorithms;


    public List<LoanOfferDto> calculatePrescoring(LoanStatementRequestDto request) {
        log.debug("Create offers input: data {}", request);
        validatePrescoring(request);
        log.info("Create offers to client {} {}", request.getFirstName(), request.getLastName());
        List<LoanOfferDto> result =
                prescoringAlgorithms.stream()
                        .map(a -> a.calculatePrescoring(request))
                        .collect(Collectors.toList());

        log.info("Prescoring sorting result by rate descending");
        result.sort(Comparator.comparing(LoanOfferDto::getRate,
                (x1, x2) -> x2.subtract(x1).compareTo(BigDecimal.valueOf(0))));
        log.info("Prescoring result {}", result);
        return result;
    }

    /**
     * Валидация клиента для прескоринга.
     * Если клиент не подходит под требования, то выбрасывается исключение {@link CreditDeniedException}.
     *
     * @param requestDto данные клиента
     */
    private void validatePrescoring(LoanStatementRequestDto requestDto) {
        log.info("Validate prescoring client {} {}", requestDto.getFirstName(), requestDto.getLastName());
        Period age = Period.between(requestDto.getBirthdate(), LocalDate.now());
        if (age.getYears() < 18) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.badAge);
        }
    }

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.debug("Calculate credit input: {}", scoringData);
        validateScoring(scoringData);
        BigDecimal rate = calculateRate(baseRate, scoringData).divide(BigDecimal.valueOf(100), mcCount);
        BigDecimal monthlyPayment = calculateMonthlyPayment(scoringData.getAmount(), rate, scoringData.getTerm());

        return CreditDto.builder()
                .amount(scoringData.getAmount().setScale(scalePrint, RoundingMode.HALF_UP))
                .term(scoringData.getTerm())
                .rate(rate.multiply(BigDecimal.valueOf(100)).setScale(scalePrint, RoundingMode.HALF_UP))
                .monthlyPayment(monthlyPayment.setScale(scalePrint, RoundingMode.HALF_UP))
                .psk(calculatePsk(monthlyPayment, scoringData.getTerm(), scoringData.getIsInsuranceEnabled()).setScale(scalePrint, RoundingMode.HALF_UP))
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .paymentSchedule(calculatePaymentSchedule(scoringData.getAmount(), rate, scoringData.getTerm(), monthlyPayment))
                .build();
    }

    /**
     * Валидация клиента для получения кредита.
     * Если клиент не подходит под требования, то выбрасывается исключение {@link CreditDeniedException}.
     *
     * @param scoringData данные клиента
     */
    private void validateScoring(ScoringDataDto scoringData) {
        log.info("Checking scoring person data is he possible to get credit");
        if (EmploymentStatus.UNEMPLOYED.equals(scoringData.getEmployment().getEmploymentStatus())) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.unemployed);
        }
        if (!scoringData.getEmployment().getSalary().equals(BigDecimal.valueOf(0))
                && scoringData.getAmount().divide(scoringData.getEmployment().getSalary(), mcCount).compareTo(BigDecimal.valueOf(25)) >= 0) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.amountIsTooBig);
        }
        if (scoringData.getEmployment().getSalary().equals(BigDecimal.valueOf(0))) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.zeroSalary);
        }
        Period age = Period.between(scoringData.getBirthdate(), LocalDate.now());
        if (age.getYears() < 20 || age.getYears() > 65) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.badAge);
        }
        if (scoringData.getEmployment().getWorkExperienceTotal() < 18) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.smallWorkExperience);
        }
        if (scoringData.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new CreditDeniedException(ErrorMessages.denied, ErrorMessages.smallWorkExperienceCurrent);
        }
    }

    /**
     * Подсчет ставки кредита на информации о клиенте.
     *
     * @param baseRate    базовая ставка в процентах
     * @param scoringData данные о клиенте
     * @return ставка в процентах (например, 15)
     */
    private BigDecimal calculateRate(BigDecimal baseRate, ScoringDataDto scoringData) {
        log.info("Calculate client personal rate");
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
     * c - количество платежей.
     *
     * @param amount         сумма под кредит
     * @param rate           годовая ставка дробью (например, 0.12)
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
     * Расчет полной стоимости кредита по формуле:
     * PSK = monthlyPayment * term + insuranceAnnuallyPrice * (term / 12)
     *
     * @param monthlyPayment ежемесячная оплата кредита
     * @param term           количество месяцев
     * @return полная стоимость кредита
     */
    private BigDecimal calculatePsk(BigDecimal monthlyPayment, int term, boolean isInsuranceEnabled) {
        //   считаем все выплаты
        BigDecimal payment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        //   считаем ежегодную страховку
        if (isInsuranceEnabled) {
            payment = payment.add(insurancePrice.multiply(BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), mcCount)));
        }
        return payment;
    }

    /**
     * Расчет графика платежей.
     *
     * @param amount         сумма займа
     * @param rate           годовая ставка дробью (например, 0.12)
     * @param term           количество месяцев
     * @param monthlyPayment ежемесячная оплата
     * @return описание каждого необходимого платежа в {@link PaymentScheduleElementDto}
     */
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
