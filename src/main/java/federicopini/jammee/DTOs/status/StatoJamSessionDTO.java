package federicopini.jammee.DTOs.status;

import jakarta.validation.constraints.NotNull;

public record StatoJamSessionDTO (@NotNull(message = "Lo stato non può essere vuoto")
                                 String stato){
}
