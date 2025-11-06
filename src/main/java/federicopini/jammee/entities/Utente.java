package federicopini.jammee.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import federicopini.jammee.entities.types.TipoUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "utenti")
@JsonIgnoreProperties({"password"})
public class Utente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    @Column(name = "data_di_registrazione")
    @Setter(AccessLevel.NONE)
    private LocalDate dataRegistrazione;
    @Column(name = "data_di_nascita")
    private LocalDate dataNascita;
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoUtente tipo;

    public Utente(String username, String nome, String cognome, String email, String password, String telefono, LocalDate dataRegistrazione, LocalDate dataNascita, TipoUtente tipo) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.dataRegistrazione = dataRegistrazione;
        this.dataNascita = dataNascita;
        this.tipo = tipo;
    }
}
