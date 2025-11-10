package federicopini.jammee.services;

import federicopini.jammee.DTOs.dimestichezza.DimestichezzaDTO;
import federicopini.jammee.DTOs.dimestichezza.UpdatedDimestichezzaDTO;
import federicopini.jammee.entities.Dimestichezza;
import federicopini.jammee.entities.Genere;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.DimestichezzaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DimestichezzaService {
    @Autowired
    private DimestichezzaRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    @Autowired
    private GenereService genereService;

    public Dimestichezza findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Dimestichezza con id " + id + " non trovato"));
    }

    public Page<Dimestichezza> getAll(int pageNumber, int pageSize, String sortBy){
    if (pageSize > 30) pageSize = 30;
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
    return this.repo.findAll(pageable);
    }

    public Dimestichezza addDimestichezza(DimestichezzaDTO body){
        if(this.repo.existsByMusicistaIdAndGenereId(body.musicistaID(),body.genereID())) throw new AlreadyExistingException("Ci sono già i dati sulla dimestichezza per questa combinazione Musicista/Genere");
        Musicista musicistaFound = this.musicistaService.findById(body.musicistaID());
        Genere genereFound = this.genereService.findById(body.genereID());
        Dimestichezza newDimestichezza = new Dimestichezza(musicistaFound,genereFound, body.voto(), body.note());
        return this.repo.save(newDimestichezza);
    }

    public Dimestichezza updateDimestichezza(UUID id, UpdatedDimestichezzaDTO body){
        Dimestichezza found = this.findById(id);
        found.setVoto(body.voto());
        found.setNote(body.note());
        return this.repo.save(found);
    }

    public void deleteDimestichezza(UUID id){
        Dimestichezza found = this.findById(id);
        this.repo.delete(found);
    }


}
