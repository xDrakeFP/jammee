package federicopini.jammee.DTOs.generi;

import jakarta.validation.constraints.NotBlank;

public record GenereDTO(@NotBlank(message = "Il nome del genere non può essere vuoto") String genere) {
}
