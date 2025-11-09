package federicopini.jammee.DTOs.errors;

import java.time.LocalDateTime;

public record ErrorsDTO (String message, LocalDateTime date) {
}
