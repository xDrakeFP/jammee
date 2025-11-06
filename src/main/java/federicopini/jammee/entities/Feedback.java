package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "valutazione")
    private int voto;

    private String note;

    @ManyToOne
    private Musicista mittente;

    @ManyToOne
    private Musicista destinatario;

    private Instant timestamp;

    public Feedback(int voto, String note, Musicista mittente, Musicista destinatario, Instant timestamp) {
        this.voto = voto;
        this.note = note;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.timestamp = timestamp;
    }
}
