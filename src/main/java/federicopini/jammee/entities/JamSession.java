package federicopini.jammee.entities;

import federicopini.jammee.entities.status.StatoJamSession;
import federicopini.jammee.entities.types.TipoJamSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "jam_sessions")
public class JamSession {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private LocalDate data;

    private String posizione;
    private String indirizzo;
    private String note;


    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoJamSession tipo;

    @ManyToOne
    @JoinColumn(name = "stato_id")
    private StatoJamSession stato;

    public JamSession(LocalDate data, String posizione, String indirizzo, String note, TipoJamSession tipo, StatoJamSession stato) {
        this.data = data;
        this.posizione = posizione;
        this.indirizzo = indirizzo;
        this.note = note;
        this.tipo = tipo;
        this.stato = stato;
    }
}
