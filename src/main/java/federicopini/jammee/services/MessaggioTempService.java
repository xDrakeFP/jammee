package federicopini.jammee.services;

import federicopini.jammee.DTOs.chat.MessaggioTempDTO;
import federicopini.jammee.entities.MessaggioTemp;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.exceptions.UnauthorizedException;
import federicopini.jammee.repos.MessaggioTempRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessaggioTempService {

    @Autowired
    private MessaggioTempRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    public MessaggioTemp findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessun messaggio trovato con Id indicato"));
    }

    public MessaggioTemp create(UUID id,MessaggioTempDTO body){
        Musicista destinatario = this.musicistaService.findById(body.destinatarioId());
        Musicista mittente = this.musicistaService.findByUtenteId(id);
        MessaggioTemp newMessaggioTemp = new MessaggioTemp(mittente,destinatario, body.contenuto());
        return this.repo.save(newMessaggioTemp);
    }

    public void delete(Utente utente, UUID id){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utente.getId());
        MessaggioTemp found = this.findById(id);
        if(foundMusicista.getId()!=found.getMittente().getId()) throw new UnauthorizedException("Non sei autorizzato a cancellare messaggi mandati da qualcun'altro");
        this.repo.delete(found);
    }
}
