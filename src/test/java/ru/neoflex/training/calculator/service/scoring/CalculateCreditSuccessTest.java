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
public class CalculateCreditSuccessTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringService scoringService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccess() throws JsonProcessingException {
        ScoringDataDto scoringData = mapper.readValue(scoringDataInput, ScoringDataDto.class);
        scoringData.setBirthdate(LocalDate.now().minusYears(20));
        var result = scoringService.calculateCredit(scoringData);
        System.out.println(mapper.writeValueAsString(result));
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
                "employmentStatus": "EMPLOYED",
                "position": "LOW",
                "salary": "1000",
                "workExperienceCurrent": 10,
                "workExperienceTotal": 20
              },
              "firstName": "ABCD",
              "gender": "MALE",
              "isInsuranceEnabled": false,
              "isSalaryClient": false,
              "lastName": "CBD",
              "maritalStatus": "SINGLE",
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
               "isInsuranceEnabled": false,
               "isSalaryClient": false,
               "monthlyPayment": "1715.61394",
               "paymentSchedule": [
                 {
                   "date": "2024-07-01",
                   "debtPayment": "1632.28061",
                   "interestPayment": "83.33333",
                   "number": 1,
                   "remainingDebt": "8367.71939",
                   "totalPayment": "1715.61394"
                 },
                 {
                   "date": "2024-08-01",
                   "debtPayment": "1645.88295",
                   "interestPayment": "69.73099",
                   "number": 2,
                   "remainingDebt": "6721.83644",
                   "totalPayment": "3431.22788"
                 },
                 {
                   "date": "2024-09-01",
                   "debtPayment": "1659.59864",
                   "interestPayment": "56.01530",
                   "number": 3,
                   "remainingDebt": "5062.23781",
                   "totalPayment": "5146.84183"
                 },
                 {
                   "date": "2024-10-01",
                   "debtPayment": "1673.42863",
                   "interestPayment": "42.18532",
                   "number": 4,
                   "remainingDebt": "3388.80918",
                   "totalPayment": "6862.45577"
                 },
                 {
                   "date": "2024-11-01",
                   "debtPayment": "1687.37387",
                   "interestPayment": "28.24008",
                   "number": 5,
                   "remainingDebt": "1701.43531",
                   "totalPayment": "8578.06971"
                 },
                 {
                   "date": "2024-12-01",
                   "debtPayment": "1701.43531",
                   "interestPayment": "14.17863",
                   "number": 6,
                   "remainingDebt": "0.00000",
                   "totalPayment": "10293.68365"
                 }
               ],
               "psk": "10293.68365",
               "rate": "10.00000",
               "term": 6
             }
            """;
}
