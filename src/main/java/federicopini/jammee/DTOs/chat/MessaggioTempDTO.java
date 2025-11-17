package federicopini.jammee.DTOs.chat;

import jakarta.validation.constraints.NotBlank;


public record MessaggioTempDTO (
    @NotBlank(message = "Il messaggio deve avere un destinatario")
    String destinatarioId,
    @NotBlank(message = "Il messaggio non può essere vuoto")
    String contenuto
)
{}
