package federicopini.jammee.DTOs.competenza;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompetenzaDTO (
        @NotNull
        UUID musicistaId,
        @NotNull
        UUID strumentoId,
        @NotNull
        @Min(value = 0)
        @Max(value = 5)
        int voto,
        String note
) {
}
