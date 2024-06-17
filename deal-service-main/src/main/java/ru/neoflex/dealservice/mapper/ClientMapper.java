package ru.neoflex.dealservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.neoflex.dealservice.dto.LoanStatementRequestDto;
import ru.neoflex.dealservice.model.Client;
import ru.neoflex.dealservice.model.Employment;
import ru.neoflex.dealservice.model.Passport;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "id", ignore = true)
    Client toEntity(LoanStatementRequestDto dto);

//    @Mapping(target = "id", ignore = true)
//    Passport toEntity(PassportDto dto);

//    @Mapping(target = "id", ignore = true)
//    Employment toEntity(EmploymentDto dto);
}
