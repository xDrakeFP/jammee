package federicopini.jammee.services.types;

import federicopini.jammee.DTOs.types.TipoUtenteDTO;
import federicopini.jammee.entities.types.TipoUtente;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.types.TipoUtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoUtenteService {

    @Autowired
    private TipoUtenteRepo repo;

    public TipoUtente findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Tipo non trovato con id indicato"));
    }

    public TipoUtente findByTipo(String tipo){
        return this.repo.findByTipo(tipo).orElseThrow(()-> new NotFoundException("Tipo non valido"));
    }

    public TipoUtente create(TipoUtenteDTO body) {
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Tipo gia esistente");
        TipoUtente tipoUtente = new TipoUtente(body.tipo());
        return this.repo.save(tipoUtente);
    }

    public TipoUtente update(UUID id, TipoUtenteDTO body){
        if(this.repo.existsByTipo(body.tipo())) throw new AlreadyExistingException("Tipo gia esistente");
        TipoUtente found = this.findByTipo(body.tipo());
        found.setTipo(body.tipo());
        return this.repo.save(found);
    }

    public void delete(UUID id){
        TipoUtente found = this.findById(id);
        this.repo.delete(found);
    }
}
