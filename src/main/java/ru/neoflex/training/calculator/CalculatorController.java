package ru.neoflex.training.calculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.training.calculator.model.LoanOfferDto;
import ru.neoflex.training.calculator.model.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.util.List;

@RestController("/calculator")
@Slf4j
public class CalculatorController {

    @PostMapping("/offers")
    public List<LoanOfferDto> calculatePrescoring(@RequestBody LoanStatementRequestDto loanRequest) {
        log.info("New loan statement request {}", loanRequest);
        return List.of(LoanOfferDto.builder()
                        .totalAmount(BigDecimal.valueOf(100_000))
                        .build(),
                LoanOfferDto.builder()
                        .totalAmount(BigDecimal.valueOf(200_000))
                        .build());
    }

    @PostMapping("/calc")
    public void calculateScoring() {

    }
}
