package federicopini.jammee.services;

import federicopini.jammee.DTOs.partecipazione.PartecipazioneDTO;
import federicopini.jammee.DTOs.partecipazione.PartecipazioneJamSessionDTO;
import federicopini.jammee.entities.*;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.BadRequestException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.exceptions.UnauthorizedException;
import federicopini.jammee.repos.JamSessionRepo;
import federicopini.jammee.repos.PartecipazioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PartecipazioneService {

    @Autowired
    private PartecipazioneRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    @Autowired
    private JamSessionRepo jamSessionRepo;

    public Partecipazione findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Partecipazione non trovata con ID indicato"));
    }

    public Partecipazione addPartecipazione(PartecipazioneDTO body, Utente utente){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utente.getId());
        if(this.repo.existsByMusicistaIdAndJamSessionId(foundMusicista.getId(),body.jamSessionId())) throw new AlreadyExistingException("Esiste gia una partecipazione per questo utente a questa Jam");
        JamSession foundJamSession = this.jamSessionRepo.findById(body.jamSessionId()).orElseThrow(()-> new NotFoundException("Nessuna Jam session trovata con l'id indicato"));
        Partecipazione newPartecipazione = new Partecipazione(foundMusicista,foundJamSession);
        return this.repo.save(newPartecipazione);
    }

    public Page<Partecipazione> getByMusicistaId(UUID id,int pageNumber, int pageSize, String sortBy){
        Musicista found = this.musicistaService.findByUtenteId(id);
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findByMusicistaId(found.getId(),pageable);
    }

    public Partecipazione confirmPartecipazione(UUID id,Utente utente){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utente.getId());
        Partecipazione found = this.findById(id);
        if(foundMusicista.getId()!= found.getMusicista().getId()) throw new UnauthorizedException("Non sei autorizzato a modificare Jam Session che non hai creato");

        if(found.isConfermata()) throw new BadRequestException("Partecipazione già confermata");
        found.setConfermata(true);
        return this.repo.save(found);
    }

    public Partecipazione undoPartecipazione(UUID id,Utente utente){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utente.getId());
        Partecipazione found = this.findById(id);
        if(foundMusicista.getId()!= found.getMusicista().getId()) throw new UnauthorizedException("Non sei autorizzato a modificare Jam Session che non hai creato");
        if(!found.isConfermata()) throw new BadRequestException("Partecipazione non ancora confermata");
        if(foundMusicista.getId() == found.getJamSession().getCreatore().getId()) throw new BadRequestException("Non puoi annullare la partecipazione ad una Jam Session creata da te");
        found.setConfermata(false);
        return this.repo.save(found);
    }

    public void deletePartecipazione(UUID id, Utente utente){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utente.getId());
        Partecipazione found = this.findById(id);
        if(foundMusicista.getId()!= found.getMusicista().getId()) throw new UnauthorizedException("Non sei autorizzato a modificare Jam Session che non hai creato");
        if(foundMusicista.getId() == found.getJamSession().getCreatore().getId()) throw new BadRequestException("Non puoi cancellare una partecipazione ad una jam session create da te");
        this.repo.delete(found);
    }

    public Page<Partecipazione> getByJamSessionId(UUID jamSessionId,int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findByJamSessionId(jamSessionId,pageable);
    }




}
