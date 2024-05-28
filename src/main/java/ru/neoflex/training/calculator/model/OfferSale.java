package ru.neoflex.training.calculator.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferSale {
    private Integer ratePointsDecrease;
    private BigDecimal amountMultiply;
    private BigDecimal tempMultiply;
}
