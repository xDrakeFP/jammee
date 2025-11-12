package federicopini.jammee.DTOs.types;

import jakarta.validation.constraints.NotNull;

public record TipoJamSessionDTO(
        @NotNull(message = "Il tipo non può essere vuoto")
        String tipo
) {
}
