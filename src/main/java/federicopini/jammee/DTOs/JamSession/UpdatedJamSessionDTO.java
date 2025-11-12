package federicopini.jammee.DTOs.JamSession;

import jakarta.validation.constraints.NotNull;

public record UpdatedJamSessionDTO(
        @NotNull(message = "La posizione non può essere vuota")
        String posizione,
        @NotNull(message = "L'indirizzo non può essere vuoto")
        String indirizzo,
        String note
) {
}
