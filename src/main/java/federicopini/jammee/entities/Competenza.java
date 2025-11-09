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
@Table(name = "competenze")
public class Competenza {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "musicista_id")
    private Musicista musicista;

    @ManyToOne
    @JoinColumn(name = "strumento_id")
    private Strumento strumento;

    @Column(name = "competenza")
    private int voto;

    private String note;

    public Competenza(Musicista musicista, Strumento strumento, int voto, String note) {
        this.musicista = musicista;
        this.strumento = strumento;
        this.voto = voto;
        this.note = note;
    }
}
