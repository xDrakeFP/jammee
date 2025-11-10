package federicopini.jammee.DTOs.dimestichezza;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdatedDimestichezzaDTO(
        @Min(value = 0)
        @Max(value = 5)
        int voto,
        String note
) {
}
