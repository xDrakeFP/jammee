package federicopini.jammee.DTOs.dimestichezza;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DimestichezzaDTO (
        @NotNull UUID genereId,
        @NotNull
        @Min(value = 0)
        @Max(value = 5)
        int voto,
        String note
)
{
}
