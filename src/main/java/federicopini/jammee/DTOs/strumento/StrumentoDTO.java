package federicopini.jammee.DTOs.strumento;

import jakarta.validation.constraints.NotNull;

public record StrumentoDTO (
        @NotNull String nome,
        String descrizione,
        @NotNull String tipo
) {
}
