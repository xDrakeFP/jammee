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

import java.util.Objects;
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

    public Page<Dimestichezza> getByUserId(int pageNumber, int pageSize, String sortBy, UUID utenteId){
        Musicista found = this.musicistaService.findByUtenteId(utenteId);
        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(sortBy).ascending());
        return repo.findByMusicistaId(found.getId(),pageable);
    }

    public Dimestichezza addDimestichezza(DimestichezzaDTO body, UUID utenteId){
        Musicista musicistaFound = this.musicistaService.findByUtenteId(utenteId);
        if(this.repo.existsByMusicistaIdAndGenereId(musicistaFound.getId(),body.genereID())) throw new AlreadyExistingException("Ci sono già i dati sulla dimestichezza per questa combinazione Musicista/Genere");
        Genere genereFound = this.genereService.findById(body.genereID());
        Dimestichezza newDimestichezza = new Dimestichezza(musicistaFound,genereFound, body.voto(), body.note());
        return this.repo.save(newDimestichezza);
    }

    public Dimestichezza updateDimestichezza(UpdatedDimestichezzaDTO body, UUID utenteId){
        Musicista musicistaFound = this.musicistaService.findByUtenteId(utenteId);
        Dimestichezza dimestichezzaFound = this.repo.findByMusicistaIdAndGenereId(musicistaFound.getId(),body.genereId()).orElseThrow(()-> new NotFoundException("Nessuna dimestichezza registrata per questo genere dall'utente indicato"));
        if(body.voto()!= dimestichezzaFound.getVoto()) dimestichezzaFound.setVoto(body.voto());
        if(!Objects.equals(body.note(), dimestichezzaFound.getNote())) dimestichezzaFound.setNote(body.note());
        return this.repo.save(dimestichezzaFound);
    }

    public void deleteDimestichezza(UUID genereId,UUID utenteId){
        Musicista musicistaFound = this.musicistaService.findByUtenteId(utenteId);
        Dimestichezza found = this.repo.findByMusicistaIdAndGenereId(musicistaFound.getId(),genereId).orElseThrow(()-> new NotFoundException("Nessuna dimestichezza registrata per questo genere dall'utente indicato"));
        this.repo.delete(found);
    }


}
