package ru.neoflex.training.calculator.service.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.training.calculator.configuration.ScoringConfiguration;
import ru.neoflex.training.calculator.exception.CreditDeniedException;
import ru.neoflex.training.calculator.exception.ErrorMessages;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.service.implementation.ScoringServiceImpl;

@SpringBootTest
public class CalculateCreditWorkExpCurrentTest {

    @Mock
    private ScoringConfiguration scoringConfiguration;

    @Autowired
    private ScoringServiceImpl scoringService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testSuccess() throws JsonProcessingException {
        ScoringDataDto scoringData = mapper.readValue(scoringDataInput, ScoringDataDto.class);
        scoringData.setBirthdate(LocalDate.now().minusYears(20));
        scoringData.getEmployment().setWorkExperienceCurrent(0);
        CreditDeniedException resultException = assertThrows(CreditDeniedException.class, () -> scoringService.calculateCredit(scoringData));
        assertEquals(ErrorMessages.smallWorkExperienceCurrent, resultException.getReason());
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
}
