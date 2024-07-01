package ru.neoflex.statementmc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.statementmc.dto.LoanOfferDto;
import ru.neoflex.statementmc.dto.LoanStatementRequestDto;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class StatementService {

    @Value(value = "${deal.url}")
    private String dealUrl;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(StatementService.class);

    public StatementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<LoanOfferDto> createStatement(LoanStatementRequestDto requestDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = dealUrl + "/statement";
        LoanOfferDto[] offersArray = restTemplate.postForObject(url, requestDto, LoanOfferDto[].class);
        List<LoanOfferDto> offers = Arrays.asList(offersArray);

        offers.sort(Comparator.comparing(LoanOfferDto::getRate));

        return offers;
    }

    public void selectOffer(LoanOfferDto offerDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = dealUrl + "/offer/select";
        restTemplate.postForObject(url, offerDto, Void.class);
    }
}