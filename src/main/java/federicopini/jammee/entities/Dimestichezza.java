package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dimestichezze")
public class Dimestichezza {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "musicista_id")
    private Musicista musicista;
    @ManyToOne
    @JoinColumn(name = "genere_id")
    private Genere genere;
    @Column(name = "dimestichezza")
    private int voto;
    private String note;

    public Dimestichezza(Musicista musicista, Genere genere, int voto, String note) {
        this.musicista = musicista;
        this.genere = genere;
        this.voto = voto;
        this.note = note;
    }
}
