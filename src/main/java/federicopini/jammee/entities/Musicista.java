package federicopini.jammee.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "musicisti")
public class Musicista {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    private String avatar;
    private String bio;
    private String posizione;
    private String indirizzo;
    @Column(name = "può ospitare")
    private boolean canHost;

    public Musicista(Utente utente, String avatar, String bio, String posizione, String indirizzo, boolean canHost) {
        this.utente = utente;
        this.avatar = Objects.requireNonNullElseGet(avatar, () -> "https://ui-avatars.com/api/?name=" + utente.getNome() + "+" + utente.getCognome());
        this.bio = bio;
        this.posizione = posizione;
        this.indirizzo = indirizzo;
        this.canHost = canHost;
    }
}
