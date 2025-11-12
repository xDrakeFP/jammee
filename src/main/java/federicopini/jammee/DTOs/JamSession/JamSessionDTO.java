package federicopini.jammee.DTOs.JamSession;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record JamSessionDTO(
        @FutureOrPresent(message = "La data della Jam session non può essere nel passato")
        LocalDate data,
        @NotNull(message = "La posizione non può essere vuota")
        String posizione,
        @NotNull(message = "L'indirizzo non può essere vuoto")
        String indirizzo,
        String note,
        @NotNull(message = "Il tipo di Jam Session")
        String tipo
) {
}
