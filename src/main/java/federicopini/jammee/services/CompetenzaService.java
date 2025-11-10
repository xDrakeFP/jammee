package federicopini.jammee.services;

import federicopini.jammee.DTOs.competenza.CompetenzaDTO;
import federicopini.jammee.entities.Competenza;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Strumento;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.CompetenzaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompetenzaService {

    @Autowired
    private CompetenzaRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    @Autowired
    private StrumentoService strumentoService;

    public Competenza findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Competenza con id "+id+" non trovato"));
    }

    public Page<Competenza> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Competenza createCompetenza(CompetenzaDTO body){
        if(this.repo.existsByMusicistaIdAndStrumentoId(body.musicistaId(),body.strumentoId())) throw new AlreadyExistingException("Esiste già un record Competenza per questa combinazione di utente e strumento");
        Musicista foundMusicista = this.musicistaService.findById(body.musicistaId());
        Strumento foundStrumento = this.strumentoService.findById(body.strumentoId());
        Competenza newCompetenza = new Competenza(foundMusicista,foundStrumento, body.voto(), body.note());
        return this.repo.save(newCompetenza);
    }
}
