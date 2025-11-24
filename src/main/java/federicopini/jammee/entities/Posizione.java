package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "posizioni")
@Getter
@Setter
@NoArgsConstructor
public class Posizione {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "musicista_id")
    private Musicista musicista;

    private Double latitudine;

    private Double longitudine;

    private Double accuracy;

    private Instant timestamp;

    public Posizione(Musicista musicista, Double latitudine, Double longitudine, Double accuracy) {
        this.musicista = musicista;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.accuracy = accuracy;
        this.timestamp = Instant.now();
    }
}
