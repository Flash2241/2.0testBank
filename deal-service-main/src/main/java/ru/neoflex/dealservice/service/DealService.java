package ru.neoflex.dealservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.dealservice.dto.LoanOfferDto;
import ru.neoflex.dealservice.dto.LoanStatementRequestDto;
import ru.neoflex.dealservice.mapper.ClientMapper;
import ru.neoflex.dealservice.model.ApplicationStatus;
import ru.neoflex.dealservice.model.Client;
import ru.neoflex.dealservice.model.Statement;
import ru.neoflex.dealservice.repository.ClientRepository;
import ru.neoflex.dealservice.repository.StatementRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final ClientMapper clientMapper = ClientMapper.INSTANCE;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto) {
        Client client = clientMapper.toEntity(dto);
        System.out.println("client = " + client);
        client = clientRepository.save(client);

        Statement statement = new Statement(client);
        statement = statementRepository.save(statement);

        String calculatorServiceUrl = "http://localhost:8081/calculator/offers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(dto, headers);
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                calculatorServiceUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        List<LoanOfferDto> loanOffers = response.getBody();
        for (LoanOfferDto offer : loanOffers) {
            offer.setStatementId(statement.getId());
        }

        // Сортировка LoanOfferDto от "худшего" к "лучшему" (по возрастанию ставки процента)
//        loanOffers.sort((o1, o2) -> o1.getInterestRate().compareTo(o2.getInterestRate()));

        return loanOffers;
    }

    public void processOfferSelect(LoanOfferDto loanOfferDto) {
        System.out.println("loanOfferDto = " + loanOfferDto);
        Statement statement = statementRepository.findById(loanOfferDto.getStatementId())
                .orElseThrow(() -> new RuntimeException("Statement not found"));

        statement.setStatus(ApplicationStatus.APPROVED);
        List<ApplicationStatus> statusHistory = statement.getStatusHistory();
        if (statusHistory == null) statusHistory = new ArrayList<>();
        statusHistory.add(ApplicationStatus.APPROVED);
        statement.setStatusHistory(statusHistory);
        List<LoanOfferDto> appliedOffer = statement.getAppliedOffer();
        if (appliedOffer == null) appliedOffer = new ArrayList<>();
        appliedOffer.add(loanOfferDto);
        statement.setAppliedOffer(appliedOffer);

        // 4. Заявка сохраняется.
        statementRepository.save(statement);
    }
}
