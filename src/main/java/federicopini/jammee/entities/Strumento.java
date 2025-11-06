package federicopini.jammee.entities;

import federicopini.jammee.entities.types.TipoStrumento;
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
@Table(name = "strumenti")
public class Strumento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String descrizione;
    @ManyToOne
    private TipoStrumento tipo;

    public Strumento(String nome, String descrizione, TipoStrumento tipo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipo = tipo;
    }
}
