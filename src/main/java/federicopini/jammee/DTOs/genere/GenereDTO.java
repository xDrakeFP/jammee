package federicopini.jammee.DTOs.genere;

import jakarta.validation.constraints.NotNull;

public record GenereDTO(@NotNull(message = "Il nome del genere non può essere vuoto") String genere) {
}
