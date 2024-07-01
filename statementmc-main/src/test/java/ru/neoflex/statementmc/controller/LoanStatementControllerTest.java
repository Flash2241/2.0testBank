package ru.neoflex.statementmc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.neoflex.statementmc.controller.LoanStatementController;
import ru.neoflex.statementmc.dto.LoanOfferDto;
import ru.neoflex.statementmc.dto.LoanStatementRequestDto;
import ru.neoflex.statementmc.service.StatementService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LoanStatementControllerTest {

    @Mock
    private StatementService statementService;

    private LoanStatementController loanStatementController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loanStatementController = new LoanStatementController(statementService);
    }

    @Test
    public void testCreateStatement() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        UUID statementId = UUID.randomUUID();
        List<LoanOfferDto> offers = Arrays.asList(
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1100), 12, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1), false, false),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1050), 12, BigDecimal.valueOf(95), BigDecimal.valueOf(0.05), true, false),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1075), 12, BigDecimal.valueOf(90), BigDecimal.valueOf(0.075), false, true),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1150), 12, BigDecimal.valueOf(85), BigDecimal.valueOf(0.15), true, true)
        );
        when(statementService.createStatement(requestDto)).thenReturn(offers);

        ResponseEntity<List<LoanOfferDto>> expectedResponse = ResponseEntity.ok(offers);
        ResponseEntity<List<LoanOfferDto>> actualResponse = loanStatementController.createStatement(requestDto);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testSelectOffer() {
        UUID statementId = UUID.randomUUID();
        LoanOfferDto offerDto = new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1100), 12, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1), false, false);

        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
        ResponseEntity<Void> actualResponse = loanStatementController.selectOffer(offerDto);

        assertEquals(expectedResponse, actualResponse);
    }
}