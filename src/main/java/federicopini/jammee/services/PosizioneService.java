package federicopini.jammee.services;

import federicopini.jammee.DTOs.posizione.PosizioneDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Posizione;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.PosizioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PosizioneService {

    @Autowired
    private PosizioneRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    public Posizione findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessuna posizione trovata con l'id indicato"));
    }

    public Posizione findByMusicistaId(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.findByMusicistaId(found.getId()).orElseThrow(()->new NotFoundException("Nessuna posizione trovata per l'utente con l'id indicato "));
    }

    public Posizione saveLocation(UUID utenteId, PosizioneDTO body){
        Musicista found = this.musicistaService.findByUtenteId(utenteId);
        if(this.repo.existsByMusicistaId(found.getId())) this.repo.delete(this.findByMusicistaId(found.getId()));
        Posizione newPosizione = new Posizione(found,body.latitude(), body.longitude(), body.accuracy());
        return this.repo.save(newPosizione);
    }

    public void deleteLocation(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        Posizione foundPosizione = this.findByMusicistaId(found.getId());
        this.repo.delete(foundPosizione);
    }

}
