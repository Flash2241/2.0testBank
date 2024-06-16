package ru.neoflex.training.calculator.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Объект, описывающий скидку для клиента.
 */
@Getter
@AllArgsConstructor
public class OfferSale {
    /**
     * Показатель, на сколько пунктов должна быть снижена процентная ставка
     */
    private BigDecimal rateDecrease;
    /**
     * Множитель для доступного размера кредита от базового
     */
    private BigDecimal amountMultiply;
    /**
     * Множитель для доступного времени кредитования от базового
     */
    private BigDecimal tempMultiply;
}
