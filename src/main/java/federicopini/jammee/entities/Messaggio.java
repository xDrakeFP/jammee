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
@Table(name = "messaggi")
public class Messaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mittente_id")
    private Musicista mittente;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private String contenuto;

    private Instant timestamp;

    public Messaggio(Musicista mittente, Chat chat, String contenuto, Instant timestamp) {
        this.mittente = mittente;
        this.chat = chat;
        this.contenuto = contenuto;
        this.timestamp = timestamp;
    }
}
