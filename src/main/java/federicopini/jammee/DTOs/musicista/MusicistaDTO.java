package federicopini.jammee.DTOs.musicista;

import java.util.UUID;

public record MusicistaDTO (
        String avatar,
        String bio,
        String posizione,
        String indirizzo,
        boolean canHost
) {
}
