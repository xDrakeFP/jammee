package federicopini.jammee.DTOs.utente;

import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdatedUtenteDTO (
        String username,
        String nome,
        String cognome,
        LocalDate dataNascita
){
}
