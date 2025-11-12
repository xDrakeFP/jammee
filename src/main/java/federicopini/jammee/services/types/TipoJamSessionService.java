package federicopini.jammee.services.types;

import federicopini.jammee.DTOs.types.TipoJamSessionDTO;
import federicopini.jammee.entities.Strumento;
import federicopini.jammee.entities.types.TipoJamSession;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.types.TipoJamSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoJamSessionService {
    @Autowired
    private TipoJamSessionRepo repo;

    public TipoJamSession findById(UUID id)
    {
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Tipo Jam Session con id "+id+" non trovato"));
    }

    public TipoJamSession findByTipo(String tipo){
        return this.repo.findByTipo(tipo).orElseThrow(()-> new NotFoundException("Tipo non trovato, indicare un tipo corretto"));
    }

    public Page<TipoJamSession> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public TipoJamSession add(TipoJamSessionDTO body)
    {
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Esiste già questo tipo di Jam Session a database");
        TipoJamSession tipoJamSession = new TipoJamSession(body.tipo());
        return this.repo.save(tipoJamSession);
    }

    public TipoJamSession edit(UUID id, TipoJamSessionDTO body){
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Il nuovo nome da applicare a questo record esiste gia a database");
        TipoJamSession found = this.findById(id);
        found.setTipo(body.tipo());
        return this.repo.save(found);
    }

    public void delete(UUID id){
        TipoJamSession found = this.findById(id);
        this.repo.delete(found);
    }
}
