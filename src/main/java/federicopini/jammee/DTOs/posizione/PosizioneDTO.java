package federicopini.jammee.DTOs.posizione;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PosizioneDTO(
        @NotNull(message = "La latitudine è obbligatoria")
        @DecimalMin(value = "-90.0", message = "La Latitudine deve essere maggiore o uguale a -90")
        @DecimalMax(value = "90.0", message = "La Latitudine deve essere minore o uguale a  90")
         Double latitude,
        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "La Longitudine deve essere maggiore o uguale a  -180")
        @DecimalMax(value = "180.0", message = "La Longitudine deve essere minore o uguale a  <= 180")
        Double longitude,
        Double accuracy
) {
}
