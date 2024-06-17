package ru.neoflex.dealservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.neoflex.dealservice.dto.calculator.EmploymentPosition;
import ru.neoflex.dealservice.dto.calculator.EmploymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;
    private int workExperienceTotal;
    private int workExperienceCurrent;
}
