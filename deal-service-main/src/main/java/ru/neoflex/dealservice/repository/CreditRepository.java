package ru.neoflex.dealservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.dealservice.model.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
