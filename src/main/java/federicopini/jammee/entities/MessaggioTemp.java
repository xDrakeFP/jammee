package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MessaggioTemp {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mittente_id")
    private Musicista mittente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Musicista destinatario;

    private String contenuto;

    private boolean letto;

    private Instant timestamp;

    public MessaggioTemp(Musicista mittente, Musicista destinatario, String contenuto) {
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.contenuto = contenuto;
        this.letto = false;
        this.timestamp = Instant.now();
    }
}
