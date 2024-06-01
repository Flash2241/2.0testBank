package ru.neoflex.training.calculator.service.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.service.ScoringService;

@SpringBootTest
public class PrescoringServiceSuccessTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringService scoringService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccess() throws JsonProcessingException {
        LoanStatementRequestDto loanStatement = mapper.readValue(loanStatementInput, LoanStatementRequestDto.class);
        loanStatement.setBirthdate(LocalDate.now().minusYears(18));
        List<LoanOfferDto> result = scoringService.calculatePrescoring(loanStatement);
        for (LoanOfferDto offer : result) {
            assertNotNull(offer.getStatementId(), "statement id is null");
            if (offer.getStatementId() != null) {
                offer.setStatementId(null);
            }
        }
        List<LoanOfferDto> correctResult = mapper.readValue(loanOffersOutput, new TypeReference<List<LoanOfferDto>>() {});
        assertEquals(mapper.writeValueAsString(correctResult), mapper.writeValueAsString(result));
    }

    private static final String loanStatementInput = """
            {
              "amount": "123.123",
              "birthdate": "2006.05.31",
              "email": "abcd@abcd.com",
              "firstName": "ABCD",
              "lastName": "ABCD",
              "middleName": "ABCD",
              "passportNumber": "123456",
              "passportSeries": "1234",
              "term": 123
            }
            """;

    private static final String loanOffersOutput = """
            [
              {
                "isInsuranceEnabled": false,
                "isSalaryClient": false,
                "rate": "10",
                "requestedAmount": "123.123",
                "statementId": null,
                "term": 123,
                "totalAmount": "123.123"
              },
              {
                "isInsuranceEnabled": true,
                "isSalaryClient": false,
                "rate": "8",
                "requestedAmount": "147.7475999999999945322404215630740509368479251861572265625",
                "statementId": null,
                "term": 135,
                "totalAmount": "123.123"
              },
              {
                "isInsuranceEnabled": false,
                "isSalaryClient": true,
                "rate": "8",
                "requestedAmount": "184.6845",
                "statementId": null,
                "term": 147,
                "totalAmount": "123.123"
              },
              {
                "isInsuranceEnabled": true,
                "isSalaryClient": true,
                "rate": "4",
                "requestedAmount": "246.246",
                "statementId": null,
                "term": 246,
                "totalAmount": "123.123"
              }
            ]
            """;
}
