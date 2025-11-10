package federicopini.jammee.DTOs.dimestichezza;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdatedDimestichezzaDTO(
        @NotNull
        UUID genereId,
        @Min(value = 0)
        @Max(value = 5)
        int voto,
        String note
) {
}
