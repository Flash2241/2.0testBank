package ru.neoflex.training.calculator.controller.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.training.calculator.controller.CalculatorController;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.service.implementation.ScoringServiceImpl;

@WebMvcTest(CalculatorController.class)
public class PrescoringSuccessTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ScoringServiceImpl scoringService;

    @Test
    public void testPrescoring() throws Exception {
        List<LoanOfferDto> correctResult = mapper.readValue(loanOffersOutput, new TypeReference<List<LoanOfferDto>>() {});
        doReturn(correctResult).when(scoringService).calculatePrescoring(any());
        String request = mapper.writeValueAsString(mapper.readValue(loanStatementInput, LoanStatementRequestDto.class));
        mockMvc.perform(post("/offers").contentType(APPLICATION_JSON_UTF8).content(request))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertEquals(mapper.writeValueAsString(correctResult),
                        mapper.writeValueAsString(
                                mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<LoanOfferDto>>() {}))
                ));
    }

    private static final String loanStatementInput = """
            {
              "amount": "40000.123",
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