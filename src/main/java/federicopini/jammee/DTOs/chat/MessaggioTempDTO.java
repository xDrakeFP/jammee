package federicopini.jammee.DTOs.chat;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessaggioTempDTO (
    @NotBlank(message = "Il messaggio deve avere un destinatario")
    UUID destinatarioId,
    @NotBlank(message = "Il messaggio non può essere vuoto")
    String contenuto
)
{}
