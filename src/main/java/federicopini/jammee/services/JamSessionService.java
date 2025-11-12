package federicopini.jammee.services;

import federicopini.jammee.DTOs.JamSession.JamSessionDTO;
import federicopini.jammee.DTOs.JamSession.UpdatedJamSessionDTO;
import federicopini.jammee.DTOs.partecipazione.PartecipazioneDTO;
import federicopini.jammee.DTOs.status.StatoJamSessionDTO;
import federicopini.jammee.entities.JamSession;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Partecipazione;
import federicopini.jammee.entities.status.StatoJamSession;
import federicopini.jammee.entities.types.TipoJamSession;
import federicopini.jammee.exceptions.NotFoundException;
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

    public JamSession createJamSession(UUID id,JamSessionDTO body) {
        Musicista foundMusicista = this.musicistaService.findById(id);
        TipoJamSession found = this.tipoJamSessionService.findByTipo(body.tipo());
        StatoJamSession programmata = this.statoJamSessionService.findByStato("PROGRAMMATA");
        JamSession newJamSession = new JamSession(body.data(), body.posizione(), body.indirizzo(), body.note(), foundMusicista,found, programmata);
        PartecipazioneDTO bodyPartecipazione = new PartecipazioneDTO(foundMusicista.getId(),newJamSession.getId());
        Partecipazione partecipazioneAutore = this.partecipazioneService.addPartecipazione(bodyPartecipazione);
        this.partecipazioneService.confirmPartecipazione(partecipazioneAutore.getId());
        return this.repo.save(newJamSession);
    }

    public JamSession findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Jam session con id indicato non trovata"));
    }

    public JamSession updateJamSession(UUID id,UpdatedJamSessionDTO body){
        JamSession found = this.findById(id);
        if(!Objects.equals(found.getIndirizzo(), body.indirizzo())) found.setIndirizzo(body.indirizzo());
        if(!Objects.equals(found.getPosizione(), body.posizione())) found.setPosizione(body.posizione());
        if(!Objects.equals(found.getNote(), body.note())) found.setNote(body.note());
        return this.repo.save(found);
    }

    public JamSession updateStatus(UUID id,StatoJamSessionDTO body){
        StatoJamSession foundStatoJamSession = this.statoJamSessionService.findByStato(body.stato());
        JamSession found = this.findById(id);
        found.setStato(foundStatoJamSession);
        return this.repo.save(found);
    }

    public void deleteJamSession(UUID id){
        JamSession found = this.findById(id);
        this.repo.delete(found);
    }
}
