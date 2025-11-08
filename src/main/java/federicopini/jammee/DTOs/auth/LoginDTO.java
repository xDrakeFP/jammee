package federicopini.jammee.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO (@Email @NotBlank(message = "L'email non può essere vuota") String email,
                        @NotBlank(message = "la password non può essere vuota") String password) {}
