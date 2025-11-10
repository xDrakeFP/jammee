package federicopini.jammee.services.types;

import federicopini.jammee.entities.types.TipoStrumento;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.types.TipoStrumentoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoStrumentoService {

    @Autowired
    private TipoStrumentoRepo repo;

    public TipoStrumento findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessun tipo trovato con l'id indicato"));
    }

    public TipoStrumento findByTipo(String tipo){
        return this.repo.findByTipo(tipo).orElseThrow(()->new NotFoundException("Tipo indicato non valido"));
    }
}
