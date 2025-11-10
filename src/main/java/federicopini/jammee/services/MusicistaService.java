package federicopini.jammee.services;

import federicopini.jammee.DTOs.musicista.MusicistaDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.MusicistaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MusicistaService {
    @Autowired
    private MusicistaRepo repo;

    @Autowired
    private UtenteService utenteService;

    public Musicista findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Musicista con id "+id+" non trovato"));
    }

    public Musicista findByUtenteId(UUID id){
        return this.repo.findByUtenteId(id).orElseThrow(()-> new NotFoundException("Utente con id " + id + " non trovato"));
    }

    public Page<Musicista> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Musicista createMusicista(UUID id,MusicistaDTO body){
        if(this.repo.existsByUtenteId(id)) throw new AlreadyExistingException("Esiste già un Entita Musicista per questo Utente");
        Musicista musicista = new Musicista(this.utenteService.findById(id), body.avatar(), body.bio(), body.posizione(), body.indirizzo(), body.canHost());
        return this.repo.save(musicista);
    }

    public Musicista updateMusicista(UUID id, MusicistaDTO body){
        Musicista found = this.findByUtenteId(id);
        if(body.avatar() != null) found.setAvatar(body.avatar());
        if(body.bio() != null) found.setBio(body.bio());
        if(body.posizione() != null) found.setPosizione(body.posizione());
        if(body.indirizzo() != null) found.setIndirizzo(body.indirizzo());
        found.setCanHost(body.canHost());
        return this.repo.save(found);
    }

    public void deleteMusicista(UUID id)
    {
        Musicista found = this.findById(id);
        this.repo.delete(found);
    }
}
