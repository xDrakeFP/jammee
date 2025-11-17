package federicopini.jammee.DTOs.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FeedbackDTO(
        @NotNull(message = "Il voto non può essere nullo")
        @Min(value = 0)
        @Max(value = 5)
        int voto,
        String note,
        @NotBlank(message = "Il feedback deve avere un destinatario")
        String destinatarioId
) {
}
