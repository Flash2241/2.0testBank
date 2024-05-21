package Project.ru.Neoflex.training.centre.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStateDto {

    @NonNull
    Long id;

    @NotNull
    String name;

    @NonNull
    Long ordinal;

    @NonNull
    @JsonProperty("created_at")

    Instant createdAt = Instant.now();
}
