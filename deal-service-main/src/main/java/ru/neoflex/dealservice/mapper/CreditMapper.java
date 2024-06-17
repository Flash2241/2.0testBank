package ru.neoflex.dealservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.neoflex.dealservice.dto.calculator.CreditDto;
import ru.neoflex.dealservice.dto.calculator.PaymentScheduleElementDto;
import ru.neoflex.dealservice.model.Credit;

import java.util.List;

@Mapper
public interface CreditMapper {
    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    @Mapping(source = "paymentSchedule", target = "paymentSchedule")
    CreditDto creditToCreditDto(Credit credit);

    @Mapping(source = "paymentSchedule", target = "paymentSchedule")
    Credit creditDtoToCredit(CreditDto creditDto);
}
