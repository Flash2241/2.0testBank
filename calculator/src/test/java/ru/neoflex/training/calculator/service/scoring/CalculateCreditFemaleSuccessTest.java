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
import ru.neoflex.training.calculator.service.ScoringService;

@SpringBootTest
public class CalculateCreditFemaleSuccessTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringService scoringService;

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
                 "monthlyPayment": "1671.53115",
                 "paymentSchedule": [
                   {
                     "date": "2024-07-01",
                     "debtPayment": "1663.19782",
                     "interestPayment": "8.33333",
                     "number": 1,
                     "remainingDebt": "8336.80218",
                     "totalPayment": "1671.53115"
                   },
                   {
                     "date": "2024-08-01",
                     "debtPayment": "1664.58382",
                     "interestPayment": "6.94734",
                     "number": 2,
                     "remainingDebt": "6672.21836",
                     "totalPayment": "3343.06230"
                   },
                   {
                     "date": "2024-09-01",
                     "debtPayment": "1665.97097",
                     "interestPayment": "5.56018",
                     "number": 3,
                     "remainingDebt": "5006.24739",
                     "totalPayment": "5014.59346"
                   },
                   {
                     "date": "2024-10-01",
                     "debtPayment": "1667.35928",
                     "interestPayment": "4.17187",
                     "number": 4,
                     "remainingDebt": "3338.88811",
                     "totalPayment": "6686.12461"
                   },
                   {
                     "date": "2024-11-01",
                     "debtPayment": "1668.74875",
                     "interestPayment": "2.78241",
                     "number": 5,
                     "remainingDebt": "1670.13937",
                     "totalPayment": "8357.65576"
                   },
                   {
                     "date": "2024-12-01",
                     "debtPayment": "1670.13937",
                     "interestPayment": "1.39178",
                     "number": 6,
                     "remainingDebt": "0.00000",
                     "totalPayment": "10029.18691"
                   }
                 ],
                 "psk": "85029.18691",
                 "rate": "1.00000",
                 "term": 6
               }
            """;
}
