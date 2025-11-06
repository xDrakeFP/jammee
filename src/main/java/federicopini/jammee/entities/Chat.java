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
@Table(name = "chatrooms")
public class Chat {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String nome;

    @Column(name = "timestamp_creazione")
    private Instant timestamp;

    public Chat(String nome, Instant timestamp) {
        this.nome = nome;
        this.timestamp = timestamp;
    }
}
