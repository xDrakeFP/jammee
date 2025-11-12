package federicopini.jammee.services;

import federicopini.jammee.DTOs.strumento.StrumentoDTO;
import federicopini.jammee.DTOs.types.TipoStrumentoDTO;
import federicopini.jammee.entities.Strumento;
import federicopini.jammee.entities.types.TipoStrumento;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.StrumentoRepo;
import federicopini.jammee.services.types.TipoStrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class StrumentoService {

    @Autowired
    private StrumentoRepo repo;

    @Autowired
    private TipoStrumentoService tipoService;

    public Strumento findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Strumento con id "+id+" non trovato"));
    }

    public Page<Strumento> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Page<Strumento> getByType(TipoStrumentoDTO body,int pageNumber, int pageSize, String sortBy){
        TipoStrumento found = this.tipoService.findByTipo(body.tipo());
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findByTipoId(found.getId(),pageable);
    }

    public Strumento createStrumento(StrumentoDTO body){
        if(this.repo.existsByNome(body.nome())) throw new AlreadyExistingException("Esiste già uno strumento con questo nome");
        TipoStrumento found = this.tipoService.findByTipo(body.tipo());
        Strumento newStrumento = new Strumento(body.nome(), body.descrizione(), found);
        return this.repo.save(newStrumento);
    }

    public Strumento updateStrumento(UUID id,StrumentoDTO body){
        Strumento found = this.findById(id);
        TipoStrumento foundTipo = this.tipoService.findByTipo(body.tipo());
        if(!Objects.equals(body.nome(), found.getNome())) found.setNome(body.nome());
        if(!Objects.equals(body.descrizione(), found.getDescrizione()) && !body.descrizione().isBlank()) found.setDescrizione(body.descrizione());
        if(foundTipo != found.getTipo()) found.setTipo(foundTipo);
        return this.repo.save(found);
    }

    public void deleteStrumento(UUID id){
        this.repo.delete(this.findById(id));
    }
}
