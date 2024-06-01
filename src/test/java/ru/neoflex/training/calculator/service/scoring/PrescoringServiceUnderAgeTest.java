package ru.neoflex.training.calculator.service.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.exception.CreditDeniedException;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.service.ScoringService;


@SpringBootTest
public class PrescoringServiceUnderAgeTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringService scoringService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testUnderAge() throws JsonProcessingException {
        LoanStatementRequestDto loanStatement = mapper.readValue(loanStatementInput, LoanStatementRequestDto.class);
        CreditDeniedException resultException = assertThrows(CreditDeniedException.class, () -> scoringService.calculatePrescoring(loanStatement));
        assertEquals("bad age", resultException.getReason());
    }

    private static final String loanStatementInput = """
            {
              "amount": "123.123",
              "birthdate": "2024.05.31",
              "email": "abcd@abcd.com",
              "firstName": "ABCD",
              "lastName": "ABCD",
              "middleName": "ABCD",
              "passportNumber": "123456",
              "passportSeries": "1234",
              "term": 123
            }
            """;

    private static final String badRequestOutput = """
            """;
}
