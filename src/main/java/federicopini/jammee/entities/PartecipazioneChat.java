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
@Table(name = "partecipazioni_utenti_chat")
public class PartecipazioneChat {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "musicista_id")
    private Musicista musicista;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public PartecipazioneChat(Musicista musicista, Chat chat) {
        this.musicista = musicista;
        this.chat = chat;
    }
}
