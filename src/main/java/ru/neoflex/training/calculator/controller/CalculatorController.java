package ru.neoflex.training.calculator.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.service.ScoringService;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping
@RestController
public class CalculatorController {

    private final ScoringService scoringService;

    @PostMapping("/offers")
    public List<LoanOfferDto> calculatePrescoring(
            @Valid @RequestBody LoanStatementRequestDto loanRequest) {
        log.info("New prescoring request {}", loanRequest);
        return scoringService.calculatePrescoring(loanRequest);
    }

    @PostMapping("/calc")
    public void calculateScoring(@Valid @RequestBody ScoringDataDto scoringData) {
        log.info("New scoring request {}", scoringData);

    }
}
