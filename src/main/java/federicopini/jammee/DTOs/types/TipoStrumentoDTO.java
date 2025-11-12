package federicopini.jammee.DTOs.types;

import jakarta.validation.constraints.NotBlank;

public record TipoStrumentoDTO(@NotBlank(message = "Il tipo non può essere vuoto") String tipo) {
}
