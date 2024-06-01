package ru.neoflex.training.calculator.controller.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.training.calculator.controller.CalculatorController;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.service.ScoringService;

@WebMvcTest(CalculatorController.class)
public class CalculateCreditSuccess {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ScoringService scoringService;

    @Test
    public void testPrescoring() throws Exception {
        CreditDto correctResult = mapper.readValue(creditOutput, CreditDto.class);
        doReturn(correctResult).when(scoringService).calculateCredit(any());
        String request = mapper.writeValueAsString(mapper.readValue(scoringDataInput, ScoringDataDto.class));
        mockMvc.perform(post("/calc").contentType(APPLICATION_JSON_UTF8).content(request))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertEquals(mapper.writeValueAsString(correctResult),
                        mapper.writeValueAsString(
                                mapper.readValue(result.getResponse().getContentAsString(), CreditDto.class))
                ));
    }

    private static final String scoringDataInput = """
            {
                "amount": "500000",
                "term": "7",
                "firstName": "Dmitri",
                "lastName": "Voronin",
                "middleName": "ABCD",
                "gender": "MALE",
                "birthdate": "1960.01.25",
                "passportSeries": "1234",
                "passportNumber": "123490",
                "passportIssueDate": "2000.01.25",
                "passportIssueBranch": "123abcd",
                "maritalStatus": "SINGLE",
                "dependentAmount": "123",
                "employment": {
                    "employmentStatus": "EMPLOYED",
                    "employerINN": "1231234561",
                    "salary": "1000000",
                    "position": "LOW",
                    "workExperienceTotal": "100",
                    "workExperienceCurrent": "20"
                },
                "accountNumber": "1234567890",
                "isInsuranceEnabled": false,
                "isSalaryClient": false
            }
            """;

    private static final String creditOutput = """
            {
                 "amount": "500000",
                 "isInsuranceEnabled": false,
                 "isSalaryClient": false,
                 "monthlyPayment": "73830.00000",
                 "paymentSchedule": [
                     {
                         "date": "2024-07-01",
                         "debtPayment": "69663.30000",
                         "interestPayment": "4166.7",
                         "number": 1,
                         "remainingDebt": "430337",
                         "totalPayment": "73830"
                     },
                     {
                         "date": "2024-08-01",
                         "debtPayment": "70243.90000",
                         "interestPayment": "3586.1",
                         "number": 2,
                         "remainingDebt": "360090",
                         "totalPayment": "147660"
                     },
                     {
                         "date": "2024-09-01",
                         "debtPayment": "70829.30000",
                         "interestPayment": "3000.7",
                         "number": 3,
                         "remainingDebt": "289260",
                         "totalPayment": "221490"
                     },
                     {
                         "date": "2024-10-01",
                         "debtPayment": "71419.50000",
                         "interestPayment": "2410.5",
                         "number": 4,
                         "remainingDebt": "217840",
                         "totalPayment": "295320"
                     },
                     {
                         "date": "2024-11-01",
                         "debtPayment": "72014.70000",
                         "interestPayment": "1815.3",
                         "number": 5,
                         "remainingDebt": "145830",
                         "totalPayment": "369150"
                     },
                     {
                         "date": "2024-12-01",
                         "debtPayment": "72614.80000",
                         "interestPayment": "1215.2",
                         "number": 6,
                         "remainingDebt": "73220",
                         "totalPayment": "442980"
                     },
                     {
                         "date": "2025-01-01",
                         "debtPayment": "73219.84000",
                         "interestPayment": "610.16",
                         "number": 7,
                         "remainingDebt": "0",
                         "totalPayment": "516810"
                     }
                 ],
                 "psk": "516810.00000",
                 "rate": "10.0",
                 "term": 7
             }
            """;
}
