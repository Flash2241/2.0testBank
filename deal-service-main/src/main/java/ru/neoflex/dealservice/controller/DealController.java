package ru.neoflex.dealservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.dealservice.dto.FinishRegistrationRequestDto;
import ru.neoflex.dealservice.dto.LoanOfferDto;
import ru.neoflex.dealservice.dto.LoanStatementRequestDto;
import ru.neoflex.dealservice.service.DealService;
import ru.neoflex.dealservice.service.StatementService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;
    private final StatementService statementService;

    @PostMapping("/statement")
    public List<LoanOfferDto> statement(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return dealService.processLoanStatement(loanStatementRequestDto);
    }

    @PostMapping("/offer/select")
    public void offerSelect(@RequestBody LoanOfferDto loanOfferDto) {
        dealService.processOfferSelect(loanOfferDto);
    }

    @PostMapping("/calculate/{id}")
    public void calculate(@PathVariable UUID id, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        statementService.calculate(id, finishRegistrationRequestDto);
    }
}
