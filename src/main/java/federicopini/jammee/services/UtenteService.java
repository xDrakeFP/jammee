package federicopini.jammee.services;

import federicopini.jammee.DTOs.types.TipoUtenteDTO;
import federicopini.jammee.DTOs.utente.UpdatedUtenteDTO;
import federicopini.jammee.DTOs.utente.UtenteDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.entities.types.TipoUtente;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.BadRequestException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.UtenteRepo;
import federicopini.jammee.services.types.TipoUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepo repo;

    @Autowired
    private TipoUtenteService tipoService;

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
        TipoUtente found = this.tipoService.findByTipo(body.tipo());
        Utente utente = new Utente(body.username(), body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()), body.telefono(), body.dataNascita(),found);
    return this.repo.save(utente);
    }

    public Utente updateUser(UUID id, UpdatedUtenteDTO body){
        if(this.repo.existsByUsername(body.username())) throw new AlreadyExistingException("Username già in uso!");
        Utente found = this.findById(id);
        if(!Objects.equals(body.username(), found.getUsername())) found.setUsername(body.username());
        if(!Objects.equals(body.nome(), found.getNome())) found.setNome(body.nome());
        if(!Objects.equals(body.cognome(), found.getCognome())) found.setCognome(body.cognome());
       if(body.dataNascita()!=found.getDataNascita()) {
         if(body.dataNascita().isAfter(LocalDate.now())) throw new BadRequestException("La data di nascita non può essere nel futuro");
         found.setDataNascita(body.dataNascita());
       }

        return this.repo.save(found);
    }

    public Page<Utente> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public void deleteUser(UUID id){
        Utente found = this.findById(id);
        this.repo.delete(found);
    }

    public Utente updateTipoUtente(UUID id, TipoUtenteDTO body){
        TipoUtente tipoFound = this.tipoService.findByTipo(body.tipo());
        Utente found = this.findById(id);
        found.setTipo(tipoFound);
        return this.repo.save(found);
    }
}
