package ru.neoflex.dealservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.neoflex.dealservice.dto.FinishRegistrationRequestDto;
import ru.neoflex.dealservice.dto.calculator.ScoringDataDto;
import ru.neoflex.dealservice.model.Client;

import java.math.BigDecimal;

@Mapper
public interface ClientToScoringDataMapper {
    ClientToScoringDataMapper INSTANCE = Mappers.getMapper(ClientToScoringDataMapper.class);

    @Mapping(source = "passport.series", target = "passportSeries")
    @Mapping(source = "passport.number", target = "passportNumber")
    @Mapping(source = "passport.issueDate", target = "passportIssueDate")
    @Mapping(source = "passport.issueBranch", target = "passportIssueBranch")
    @Mapping(source = "employment.position", target = "employment.position")
    @Mapping(source = "employment.salary", target = "employment.salary")
    @Mapping(source = "employment.employerInn", target = "employment.employerINN")
    @Mapping(source = "employment.status", target = "employment.employmentStatus")
    @Mapping(source = "employment.workExperienceCurrent", target = "employment.workExperienceCurrent")
    @Mapping(source = "birthDate", target = "birthdate")
    ScoringDataDto clientToScoringDataDto(Client client);

    default ScoringDataDto map(Client client, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        ScoringDataDto dto = clientToScoringDataDto(client);
        System.out.println("dto = " + dto);
        dto.setAmount(finishRegistrationRequestDto.amount());
        dto.setTerm(finishRegistrationRequestDto.term());
        dto.setIsInsuranceEnabled(finishRegistrationRequestDto.isInsuranceEnabled());
        dto.setIsSalaryClient(finishRegistrationRequestDto.isSalaryClient());
        return dto;
    }
}
