package ru.neoflex.training.calculator.service.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.service.implementation.ScoringServiceImpl;

@SpringBootTest
public class CalculateCreditFemaleSuccessTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringServiceImpl scoringService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccess() throws JsonProcessingException {
        ScoringDataDto scoringData = mapper.readValue(scoringDataInput, ScoringDataDto.class);
        scoringData.setBirthdate(LocalDate.now().minusYears(32));
        var result = scoringService.calculateCredit(scoringData);
        CreditDto correctResult = mapper.readValue(creditOutput, CreditDto.class);
        assertEquals(mapper.writeValueAsString(correctResult), mapper.writeValueAsString(result));
    }

    private static final String scoringDataInput = """
            {
              "accountNumber": "1234567890",
              "amount": "10000",
              "birthdate": "2006.06.01",
              "dependentAmount": 2,
              "employment": {
                "employerINN": "1234567890",
                "employmentStatus": "SELF_EMPLOYED",
                "position": "MEDIUM",
                "salary": "1000",
                "workExperienceCurrent": 10,
                "workExperienceTotal": 20
              },
              "firstName": "ABCD",
              "gender": "FEMALE",
              "isInsuranceEnabled": true,
              "isSalaryClient": true,
              "lastName": "CBD",
              "maritalStatus": "MARRIED",
              "middleName": "HA",
              "passportIssueBranch": "asdjfldsakjf",
              "passportIssueDate": "2022.06.01",
              "passportNumber": "123456",
              "passportSeries": "1234",
              "term": 6
            }
            """;

    private static final String creditOutput = """
            {
                  "amount": "10000.00000",
                  "isInsuranceEnabled": true,
                  "isSalaryClient": true,
                  "monthlyPayment": "1681.28034",
                  "paymentSchedule": [
                    {
                      "date": "2024-07-03",
                      "debtPayment": "1656.28034",
                      "interestPayment": "25.00000",
                      "number": 1,
                      "remainingDebt": "8343.71966",
                      "totalPayment": "1681.28034"
                    },
                    {
                      "date": "2024-08-03",
                      "debtPayment": "1660.42104",
                      "interestPayment": "20.85930",
                      "number": 2,
                      "remainingDebt": "6683.29861",
                      "totalPayment": "3362.56069"
                    },
                    {
                      "date": "2024-09-03",
                      "debtPayment": "1664.57210",
                      "interestPayment": "16.70825",
                      "number": 3,
                      "remainingDebt": "5018.72651",
                      "totalPayment": "5043.84103"
                    },
                    {
                      "date": "2024-10-03",
                      "debtPayment": "1668.73353",
                      "interestPayment": "12.54682",
                      "number": 4,
                      "remainingDebt": "3349.99299",
                      "totalPayment": "6725.12138"
                    },
                    {
                      "date": "2024-11-03",
                      "debtPayment": "1672.90536",
                      "interestPayment": "8.37498",
                      "number": 5,
                      "remainingDebt": "1677.08762",
                      "totalPayment": "8406.40172"
                    },
                    {
                      "date": "2024-12-03",
                      "debtPayment": "1677.08762",
                      "interestPayment": "4.19272",
                      "number": 6,
                      "remainingDebt": "0.00000",
                      "totalPayment": "10087.68206"
                    }
                  ],
                  "psk": "85087.68206",
                  "rate": "3.00000",
                  "term": 6
                }
            """;
}
