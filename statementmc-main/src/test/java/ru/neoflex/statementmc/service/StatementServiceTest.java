package ru.neoflex.statementmc.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.statementmc.dto.LoanOfferDto;
import ru.neoflex.statementmc.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StatementServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private StatementService statementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        statementService = new StatementService(restTemplate);
    }

    @Test
    public void testCreateStatement() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        UUID statementId = UUID.randomUUID();
        LoanOfferDto[] offersArray = {
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1100), 12, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1), Boolean.FALSE, Boolean.FALSE),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1050), 12, BigDecimal.valueOf(95), BigDecimal.valueOf(0.05), Boolean.TRUE, Boolean.FALSE),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1075), 12, BigDecimal.valueOf(90), BigDecimal.valueOf(0.075), Boolean.FALSE, Boolean.TRUE),
                new LoanOfferDto(statementId, BigDecimal.valueOf(1000), BigDecimal.valueOf(1150), 12, BigDecimal.valueOf(85), BigDecimal.valueOf(0.15), Boolean.TRUE, Boolean.TRUE)
        };
        when(restTemplate.postForObject(eq("http://localhost:8080/deal/statement"), any(LoanStatementRequestDto.class), eq(LoanOfferDto[].class))).thenReturn(offersArray);

        List<LoanOfferDto> expectedOffers = Arrays.asList(offersArray);
        expectedOffers.sort(Comparator.comparing(LoanOfferDto::getRate));

        List<LoanOfferDto> actualOffers = statementService.createStatement(requestDto);

        assertEquals(expectedOffers, actualOffers);
    }
}
