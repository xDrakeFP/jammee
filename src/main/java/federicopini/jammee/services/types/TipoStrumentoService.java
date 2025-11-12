package federicopini.jammee.services.types;

import federicopini.jammee.DTOs.types.TipoStrumentoDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.types.TipoStrumento;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.types.TipoStrumentoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoStrumentoService {

    @Autowired
    private TipoStrumentoRepo repo;

    public TipoStrumento findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessun tipo trovato con l'id indicato"));
    }

    public Page<TipoStrumento> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Page<TipoStrumento> getAllByType(TipoStrumentoDTO body, int pageNumber, int pageSize, String sortBy){
        TipoStrumento found = this.findByTipo(body.tipo());
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findById(found.getId(), pageable);
    }

    public TipoStrumento create(TipoStrumentoDTO body){
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Il tipo esiste già");
        TipoStrumento tipoStrumento = new TipoStrumento(body.tipo());
        return this.repo.save(tipoStrumento);
    }

    public TipoStrumento update(UUID id,TipoStrumentoDTO body){
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Il tipo inserito esiste gia in un altro record");
        TipoStrumento found = this.findById(id);
        found.setTipo(body.tipo());
        return this.repo.save(found);
    }

    public void delete(UUID id){
        TipoStrumento found = this.findById(id);
        this.repo.delete(found);
    }

    public TipoStrumento findByTipo(String tipo){
        return this.repo.findByTipo(tipo).orElseThrow(()->new NotFoundException("Tipo indicato non valido"));
    }
}
