package federicopini.jammee.services;

import federicopini.jammee.DTOs.JamSession.JamSessionDTO;
import federicopini.jammee.DTOs.JamSession.UpdatedJamSessionDTO;
import federicopini.jammee.DTOs.partecipazione.PartecipazioneDTO;
import federicopini.jammee.DTOs.status.StatoJamSessionDTO;
import federicopini.jammee.entities.JamSession;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Partecipazione;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.entities.status.StatoJamSession;
import federicopini.jammee.entities.types.TipoJamSession;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.exceptions.UnauthorizedException;
import federicopini.jammee.repos.JamSessionRepo;
import federicopini.jammee.services.status.StatoJamSessionService;
import federicopini.jammee.services.types.TipoJamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class JamSessionService {

    @Autowired
    private JamSessionRepo repo;

    @Autowired
    private TipoJamSessionService tipoJamSessionService;

    @Autowired
    private StatoJamSessionService statoJamSessionService;

    @Autowired
    private MusicistaService musicistaService;

    @Autowired
    private PartecipazioneService partecipazioneService;

    @Autowired
    private UtenteService utenteService;

    public JamSession createJamSession(UUID id, JamSessionDTO body) {
        Musicista autore = this.musicistaService.findById(id);
        TipoJamSession found = this.tipoJamSessionService.findByTipo(body.tipo());
        StatoJamSession programmata = this.statoJamSessionService.findByStato("PROGRAMMATA");
        JamSession newJamSession = new JamSession(body.data(), body.posizione(), body.indirizzo(), body.note(), autore,found, programmata);
        PartecipazioneDTO bodyPartecipazione = new PartecipazioneDTO(newJamSession.getId());
        Partecipazione partecipazioneAutore = this.partecipazioneService.addPartecipazione(bodyPartecipazione,this.utenteService.findById(id));
        this.partecipazioneService.confirmPartecipazione(partecipazioneAutore.getId(),this.utenteService.findById(id));
        return this.repo.save(newJamSession);
    }

    public JamSession findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Jam session con id indicato non trovata"));
    }

    public JamSession updateJamSession(UUID id,UpdatedJamSessionDTO body, UUID utenteLoggatoId){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteLoggatoId);
        JamSession found = this.findById(id);
        if(foundMusicista.getId() != found.getCreatore().getId()) throw new UnauthorizedException("Non sei autorizzato a modificare Jam Session che non hai creato");
        if(!Objects.equals(found.getIndirizzo(), body.indirizzo())) found.setIndirizzo(body.indirizzo());
        if(!Objects.equals(found.getPosizione(), body.posizione())) found.setPosizione(body.posizione());
        if(!Objects.equals(found.getNote(), body.note())) found.setNote(body.note());
        return this.repo.save(found);
    }

    public JamSession updateStatus(UUID id,StatoJamSessionDTO body,UUID utenteLoggatoId){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteLoggatoId);
        JamSession found = this.findById(id);
        if(foundMusicista.getId()!= found.getCreatore().getId()) throw new UnauthorizedException("Non sei autorizzato a modificare Jam Session che non hai creato");
        StatoJamSession foundStatoJamSession = this.statoJamSessionService.findByStato(body.stato());
        found.setStato(foundStatoJamSession);
        return this.repo.save(found);
    }

    public void deleteJamSession(UUID id,UUID utenteLoggatoId){
        Musicista foundMusicista = this.musicistaService.findByUtenteId(utenteLoggatoId);
        JamSession found = this.findById(id);
        if(foundMusicista.getId()!= found.getCreatore().getId()) throw new UnauthorizedException("Non sei autorizzato a eliminare Jam Session che non hai creato");
        this.repo.delete(found);
    }
}
