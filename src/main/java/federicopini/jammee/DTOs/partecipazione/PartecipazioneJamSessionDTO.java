package federicopini.jammee.DTOs.partecipazione;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PartecipazioneJamSessionDTO (
        @NotNull(message = "L'id per la jam session non può essere vuoto")
        UUID jamSessionId
) {
}
