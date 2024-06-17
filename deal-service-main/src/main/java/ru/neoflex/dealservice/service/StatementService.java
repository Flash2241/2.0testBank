package ru.neoflex.dealservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.dealservice.dto.*;
import ru.neoflex.dealservice.dto.calculator.CreditDto;
import ru.neoflex.dealservice.dto.calculator.ScoringDataDto;
import ru.neoflex.dealservice.mapper.ClientMapper;
import ru.neoflex.dealservice.mapper.ClientToScoringDataMapper;
import ru.neoflex.dealservice.mapper.CreditMapper;
import ru.neoflex.dealservice.model.ApplicationStatus;
import ru.neoflex.dealservice.model.Credit;
import ru.neoflex.dealservice.model.Statement;
import ru.neoflex.dealservice.repository.CreditRepository;
import ru.neoflex.dealservice.repository.StatementRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ClientToScoringDataMapper statementMapper = ClientToScoringDataMapper.INSTANCE;
    private final CreditMapper creditMapper = CreditMapper.INSTANCE;

    //     2.	Достаётся из БД заявка(Statement) по statementId.
    ////        3.	ScoringDataDto насыщается информацией из FinishRegistrationRequestDto и Client, который хранится в Statement
    ////        4.	Отправляется POST запрос на /calculator/calc МС Калькулятор с телом ScoringDataDto через FeignClient.
    ////        5.	На основе полученного из кредитного конвейера CreditDto создаётся сущность Credit и сохраняется в базу со статусом CALCULATED.
    ////        6.	В заявке обновляется статус, история статусов.
    ////        7.	Заявка сохраняется.
    public void calculate(UUID id, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        // 2. Достаётся из БД заявка (Statement) по statementId.
        Statement statement = statementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statement not found"));

        // 3. ScoringDataDto насыщается информацией из FinishRegistrationRequestDto и Client, который хранится в Statement.
        ScoringDataDto scoringDataDto = statementMapper.map(statement.getClient(), finishRegistrationRequestDto);

        // 4. Отправляется POST запрос на /calculator/calc МС Калькулятор с телом ScoringDataDto через FeignClient.

        String calculatorServiceUrl = "http://localhost:8081/calculator/calc";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<ScoringDataDto> request = new HttpEntity<>(scoringDataDto, headers);
        System.out.println("scoringDataDto = " + scoringDataDto);
        ResponseEntity<CreditDto> response = restTemplate.exchange(
                calculatorServiceUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        CreditDto creditDto = response.getBody();
        Credit credit = creditMapper.creditDtoToCredit(creditDto);
        creditRepository.save(credit);

        // 7. Заявка сохраняется.
        statementRepository.save(statement);
    }
}
