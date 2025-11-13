package federicopini.jammee.DTOs.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdatedFeedbackDTO (@NotNull(message = "Il voto non può essere nullo")
                                  @Min(value = 0)
                                  @Max(value = 5)
                                  int voto,
                                  String note) {
}
