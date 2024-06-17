package ru.neoflex.dealservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.dealservice.model.Client;
import ru.neoflex.dealservice.model.Statement;

import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
