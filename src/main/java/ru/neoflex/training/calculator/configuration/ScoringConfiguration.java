package ru.neoflex.training.calculator.configuration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.neoflex.training.calculator.model.OfferSale;

@Getter
@Configuration
@ConfigurationProperties("scoring.offers")
public class ScoringConfiguration {

    private Map<String, OfferSale> sales = new HashMap<>();

    public void setSales(Map<String, Object> s) {
        for (Map.Entry<String, Object> sale : s.entrySet()) {
            sales.put(sale.getKey(),
                    new OfferSale(((Number) ((Map<String, Object>) sale.getValue()).get("rate-decrease")).intValue(),
                            new BigDecimal(((Number) ((Map<String, Object>) sale.getValue()).get("amount-multiply")).doubleValue()),
                            new BigDecimal(((Number) ((Map<String, Object>) sale.getValue()).get("temp-multiply")).doubleValue())));
        }
    }
}
