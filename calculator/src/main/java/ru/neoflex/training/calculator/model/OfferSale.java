package ru.neoflex.training.calculator.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferSale {
    private BigDecimal rateDecrease;
    private BigDecimal amountMultiply;
    private BigDecimal tempMultiply;
}
