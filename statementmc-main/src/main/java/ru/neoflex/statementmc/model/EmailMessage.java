package ru.neoflex.statementmc.model;

import lombok.Data;

@Data
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long statementId;

}