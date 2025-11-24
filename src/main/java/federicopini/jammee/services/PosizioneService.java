package federicopini.jammee.services;

import federicopini.jammee.DTOs.posizione.PosizioneDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Posizione;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.PosizioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Posizione> getAll(int pageNumber, int pageSize, String sortBy) {
        if(pageSize > 30 ) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Page<Posizione> getNearby(int pageNumber, int pageSize, String sortBy,double lat,double lng, double maxKm) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy).ascending());
        return this.repo.findNearby(lat,lng,maxKm,pageable);
    }

    public Posizione findByUtenteId(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.findByMusicistaId(found.getId()).orElseThrow(()->new NotFoundException("Nessuna posizione trovata per l'utente con l'id indicato "));
    }

    public Posizione saveLocation(UUID utenteId, PosizioneDTO body){
        Musicista found = this.musicistaService.findByUtenteId(utenteId);
        if(this.repo.existsByMusicistaId(found.getId())) this.repo.delete(this.findByUtenteId(found.getId()));
        Posizione newPosizione = new Posizione(found,body.latitude(), body.longitude(), body.accuracy());
        return this.repo.save(newPosizione);
    }

    public boolean existsByMusicista(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.existsByMusicistaId(found.getId());
    }


    public void deleteLocation(UUID id){

        Posizione foundPosizione = this.findByUtenteId(id);
        this.repo.delete(foundPosizione);
    }

}
