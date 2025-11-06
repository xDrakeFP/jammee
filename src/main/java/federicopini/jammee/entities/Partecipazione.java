package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "partecipazioni")
public class Partecipazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "musicista_id")
    private Musicista musicista;
    @ManyToOne
    @JoinColumn(name = "jam_session_id")
    private JamSession jamSession;

    public Partecipazione(Musicista musicista, JamSession jamSession) {
        this.musicista = musicista;
        this.jamSession = jamSession;
    }
}
