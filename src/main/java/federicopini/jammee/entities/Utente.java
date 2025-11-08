package federicopini.jammee.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import federicopini.jammee.entities.types.TipoUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "utenti")
@JsonIgnoreProperties({"password"})
public class Utente implements UserDetails {
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

    public Utente(String username, String nome, String cognome, String email, String password, String telefono, LocalDate dataNascita, TipoUtente tipo) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.dataRegistrazione = LocalDate.now();
        this.dataNascita = dataNascita;
        this.tipo = tipo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(tipo.getTipo()));
    }
}
