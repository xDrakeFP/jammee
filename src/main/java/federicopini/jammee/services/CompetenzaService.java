package federicopini.jammee.services;

import federicopini.jammee.DTOs.competenza.CompetenzaDTO;
import federicopini.jammee.DTOs.competenza.UpdatedCompetenzaDTO;
import federicopini.jammee.entities.Competenza;
import federicopini.jammee.entities.Dimestichezza;
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

import java.util.Objects;
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

    public Page<Competenza> getByUserId(int pageNumber, int pageSize, String sortBy, UUID utenteId){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteId);
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findByMusicistaId(foundMusicista.getId(),pageable);
    }

    public Competenza createCompetenza(CompetenzaDTO body, UUID utenteId){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteId);
        if(this.repo.existsByMusicistaIdAndStrumentoId(foundMusicista.getId(),body.strumentoId())) throw new AlreadyExistingException("Esiste già un record Competenza per questa combinazione di utente e strumento");
        Strumento foundStrumento = this.strumentoService.findById(body.strumentoId());
        Competenza newCompetenza = new Competenza(foundMusicista,foundStrumento, body.voto(), body.note());
        return this.repo.save(newCompetenza);
    }

    public Competenza updateCompetenza(UpdatedCompetenzaDTO body, UUID utenteId)
    {
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteId);
        Competenza foundCompetenza = this.repo.findByMusicistaIdAndStrumentoId(foundMusicista.getId(),body.strumentoId()).orElseThrow(()-> new NotFoundException("Nessuna competenza trovata per il genere e il musicista indicati"));
        if(body.voto() != foundCompetenza.getVoto()) foundCompetenza.setVoto(body.voto());
        if(!Objects.equals(body.note(), foundCompetenza.getNote())) foundCompetenza.setNote(body.note());
        return this.repo.save(foundCompetenza);
    }

    public void deleteCompetenza(UUID strumentoId, UUID utenteId){
        Musicista musicistaFound = this.musicistaService.findByUtenteId(utenteId);
        Competenza found = this.repo.findByMusicistaIdAndStrumentoId(musicistaFound.getId(),strumentoId).orElseThrow(()-> new NotFoundException("Nessuna competenza registrata per questo strumento con questo utente"));
        this.repo.delete(found);
    }




}
