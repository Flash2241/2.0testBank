package ru.neoflex.statementmc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.statementmc.dto.LoanOfferDto;
import ru.neoflex.statementmc.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "deal", url = "${deal.url}")
@Component
public interface DealClient {

    @PostMapping("/deal/statement")
    List<LoanOfferDto> calculateOffers(@RequestBody LoanStatementRequestDto requestDto);

    @PostMapping("/deal/offer/select")
    void selectOffer(@RequestBody LoanOfferDto offerDto);
}
