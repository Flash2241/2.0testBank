package ru.neoflex.statementmc.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.statementmc.dto.LoanOfferDto;
import ru.neoflex.statementmc.dto.LoanStatementRequestDto;
import ru.neoflex.statementmc.service.StatementService;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class LoanStatementController {

    private static final Logger logger = LoggerFactory.getLogger(LoanStatementController.class);

    private final StatementService statementService;


    @PostMapping
    @Operation(summary = "Create a new loan statement and calculate possible loan offers")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto requestDto) {
        logger.info("Received request to create statement: {}", requestDto);
        List<LoanOfferDto> offers = statementService.createStatement(requestDto);
        logger.info("Returning {} loan offers for statement: {}", offers.size(), requestDto);

        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer")
    @Operation(summary = "Select one of the loan offers")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto offerDto) {
        logger.info("Received request to select offer: {}", offerDto);
        statementService.selectOffer(offerDto);
        logger.info("Offer selected successfully: {}", offerDto);

        return ResponseEntity.ok().build();
    }
}