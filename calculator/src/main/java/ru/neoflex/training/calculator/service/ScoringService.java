package ru.neoflex.training.calculator.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;

@Service
public interface ScoringService {
    List<LoanOfferDto> calculatePrescoring(LoanStatementRequestDto loanStatement);
    CreditDto calculateCredit(ScoringDataDto scoringData);
}
