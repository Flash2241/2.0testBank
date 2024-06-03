package ru.neoflex.training.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Comparator;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.training.calculator.common.HttpStatusConst;
import ru.neoflex.training.calculator.model.dto.CreditDto;
import ru.neoflex.training.calculator.model.dto.LoanOfferDto;
import ru.neoflex.training.calculator.model.dto.LoanStatementRequestDto;
import ru.neoflex.training.calculator.model.dto.ScoringDataDto;
import ru.neoflex.training.calculator.openapi.OpenApiDescriptions;
import ru.neoflex.training.calculator.service.ScoringService;


@ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusConst.badRequestCode,
                description = HttpStatusConst.badRequestDescription,
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Error.class),
                        examples = {
                                @ExampleObject(name = "Пропущено обязательное поле",
                                        value = OpenApiDescriptions.MissedRequiredFields,
                                        description = "В \"error\" указано пропущенное поле"),
                                @ExampleObject(name = "Недопустимое значение поля",
                                        value = OpenApiDescriptions.BadValue)

                        }))
})
@Tag(
        name = "Calculator",
        description = "Скоринг клиентов"
)
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping
@RestController
public class CalculatorController {

    private final ScoringService scoringService;

    @Operation(summary = "Прескоринг клиента", responses = {
            @ApiResponse(responseCode = HttpStatusConst.okCode, content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CreditDto.class))
            )
            )
    })
    @PostMapping("/offers")
    public List<LoanOfferDto> calculatePrescoring(
            @Valid @RequestBody LoanStatementRequestDto loanRequest) {
        log.info("New prescoring request {}", loanRequest);
        List<LoanOfferDto> result = new ArrayList<>(scoringService.calculatePrescoring(loanRequest));
        return result;
    }

    @Operation(summary = "Расчет кредита", responses = {
            @ApiResponse(responseCode = HttpStatusConst.okCode, content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreditDto.class)
            )
        )
    })
    @PostMapping("/calc")
    public CreditDto calculateCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        log.info("New scoring request {}", scoringData);
        CreditDto creditDto = scoringService.calculateCredit(scoringData);
        log.info("Scoring credit result {}", creditDto);
        return creditDto;
    }
}
