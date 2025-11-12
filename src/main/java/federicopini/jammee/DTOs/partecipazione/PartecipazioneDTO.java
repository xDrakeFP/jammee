package federicopini.jammee.DTOs.partecipazione;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PartecipazioneDTO (
        @NotNull(message = "L'id della Jam session non può essere vuoto")
        UUID jamSessionId
){
}
