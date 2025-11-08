package federicopini.jammee.DTOs.utente;

import federicopini.jammee.validation.Phone;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UtenteDTO (
        @NotBlank(message = "Username obbligatorio!")
        String username,
        @NotBlank(message = "Nome obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere compreso tra 2 e 30 caratteri")
        String nome,
        @NotBlank(message = "Cognome obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere compreso tra 2 e 30 caratteri")
        String cognome,
        @NotBlank(message = "Email obbligatoria!")
        @Email(message = "L'email deve essere inserita nel formato corretto")
        String email,
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$\n", message = "La password deve avere: \n •Almeno una lettera maiuscola \n •Lunghezza di almeno 6 caratteri")//PASSWORD CON ALMENO UNA LETTERA MAIUSCOLA E UN NUMERO
        String password,
        @Phone
        String telefono,
        @Past(message = "La data di nascita deve essere nel passato") // TODO: Validare solo le persone maggiorenni
        LocalDate dataNascita,
        @NotBlank(message = "Il tipo non può essere vuoto!")
        String tipo
)
{
}
