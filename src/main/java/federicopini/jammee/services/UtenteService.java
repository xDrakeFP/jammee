package federicopini.jammee.services;

import federicopini.jammee.DTOs.utente.UtenteDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.UtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepo repo;

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

    public Utente registerUser(UtenteDTO body)
}
