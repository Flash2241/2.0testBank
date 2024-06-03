package ru.neoflex.training.calculator.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Calculator API",
                version = "1.0.0"
        )
)
public class OpenApiDescriptions {

    public static final String BadRequestExample =
            """
                    aasdf
                    """;

    public static final String MissedRequiredFields =
            """
                {
                    "date": "2024-05-31T15:44:05.538618+03:00",
                    "error": "Bad request",
                    "message": "'term' field is required",
                    "status": 400
                }""";

    public static final String BadValue =
            """
                {
                    "date": "2024-05-31T19:45:23.83043+03:00",
                    "error": "Bad request",
                    "message": "'amount' must be greater than or equal to 30000",
                    "status": 400
                }""";
}
