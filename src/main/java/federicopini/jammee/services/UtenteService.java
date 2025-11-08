package federicopini.jammee.services;

import federicopini.jammee.DTOs.utente.UtenteDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.entities.types.TipoUtente;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.UtenteRepo;
import federicopini.jammee.repos.types.TipoUtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepo repo;

    @Autowired
    private TipoUtenteRepo tipoRepo;

    @Autowired
    private PasswordEncoder bcrypt;

    public Utente findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Utente con id " + id + " non trovato"));
    }

    public Utente findByUsername(String username) {
        return this.repo.findByUsername(username).orElseThrow(() -> new NotFoundException("Nessun utente trovato con l'email indicata"));
    }

    public Utente findByEmail(String email) {
        return this.repo.findByEmail(email).orElseThrow(() -> new NotFoundException("Nessun utente trovato con l'email indicata"));
    }

    public Utente registerUser(UtenteDTO body){
        if(this.repo.existsByEmail(body.email())) throw new AlreadyExistingException("Email già in uso!");
        if(this.repo.existsByUsername(body.username())) throw new AlreadyExistingException("Username già in uso!");
        if(this.repo.existsByTelefono(body.telefono())) throw new AlreadyExistingException("Telefono già registrato");
        TipoUtente found = this.tipoRepo.findByTipo(body.tipo()).orElseThrow(()-> new NotFoundException("Tipo non valido"));
        Utente utente = new Utente(body.username(), body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()), body.telefono(), body.dataNascita(),found);
    return this.repo.save(utente);
    }
}
