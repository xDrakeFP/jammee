package federicopini.jammee.services.types;

import federicopini.jammee.entities.types.TipoUtente;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.types.TipoUtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoUtenteService {

    @Autowired
    private TipoUtenteRepo repo;

    public TipoUtente findByTipo(String tipo){
        return this.repo.findByTipo(tipo).orElseThrow(()-> new NotFoundException("Tipo non valido"));
    }
}
