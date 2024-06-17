package ru.neoflex.dealservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.dealservice.dto.LoanOfferDto;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private Date createdAt;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<LoanOfferDto> appliedOffer;
    private Date signDate;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ApplicationStatus> statusHistory;

    public Statement(Client client) {
        this.client = client;
    }

    public Statement() {

    }
}
