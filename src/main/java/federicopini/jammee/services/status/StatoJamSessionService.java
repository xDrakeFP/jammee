package federicopini.jammee.services.status;

import federicopini.jammee.DTOs.status.StatoJamSessionDTO;
import federicopini.jammee.entities.status.StatoJamSession;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.status.StatoJamSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StatoJamSessionService {
    @Autowired
    private StatoJamSessionRepo repo;

    public StatoJamSession findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Stato Jam Session con id "+id+" non trovato"));
    }

    public Page<StatoJamSession> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public StatoJamSession add(StatoJamSessionDTO body){
        if (this.repo.existsByStato(body.stato()))throw new AlreadyExistingException("Esiste già questo stato di Jam Session a database");
        StatoJamSession statoJamSession = new StatoJamSession(body.stato());
        return this.repo.save(statoJamSession);
    }

    public StatoJamSession edit(UUID id, StatoJamSessionDTO body){
        if(this.repo.existsByStato(body.stato())) throw new AlreadyExistingException("Il nuovo nome da applicare a questo record esiste gia a database");
        StatoJamSession found = this.findById(id);
        found.setStato(body.stato());
        return this.repo.save(found);
    }

    public void delete(UUID id){
        StatoJamSession found = this.findById(id);
        this.repo.delete(found);
    }

    public StatoJamSession findByStato(String stato){
        return this.repo.findByStato(stato).orElseThrow(()-> new NotFoundException("Stato indicato non trovato"));
    }


}
