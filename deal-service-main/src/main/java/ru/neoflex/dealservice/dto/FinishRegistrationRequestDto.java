package ru.neoflex.dealservice.dto;

import java.math.BigDecimal;

public record FinishRegistrationRequestDto(
        BigDecimal amount,
        Integer term,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {
}
