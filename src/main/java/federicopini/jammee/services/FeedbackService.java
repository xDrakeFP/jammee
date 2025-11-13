package federicopini.jammee.services;

import federicopini.jammee.DTOs.feedback.FeedbackDTO;
import federicopini.jammee.DTOs.feedback.UpdatedFeedbackDTO;
import federicopini.jammee.entities.Feedback;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.exceptions.UnauthorizedException;
import federicopini.jammee.repos.FeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepo repo;

    @Autowired
    private MusicistaService musicistaService;

    public Feedback findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessun feedback trovato con l'id indicato"));
    }

    public Feedback createFeedback(Utente utente, FeedbackDTO body){
        Musicista musicistaLoggato = this.musicistaService.findById(utente.getId());
        Musicista destinatario = this.musicistaService.findById(body.destinatarioId());
        Feedback feedback = new Feedback(body.voto(), body.note(), musicistaLoggato,destinatario);
        return this.repo.save(feedback);
    }

    public Feedback updateFeedback(Utente utente, UUID id, UpdatedFeedbackDTO body){
        Feedback found = this.findById(id);
        Musicista musicistaLoggato = this.musicistaService.findByUtenteId(id);
        if(musicistaLoggato.getId()!= found.getMittente().getId()) throw new UnauthorizedException("Non puoi modificare feedback lasciati da altri utenti");
        if(found.getVoto() != body.voto()) found.setVoto(body.voto());
        if(!Objects.equals(found.getNote(), body.note())) found.setNote(body.note());
        return this.repo.save(found);
    }

    public void deleteFeedback(Utente utente,UUID id){
        Feedback found = this.findById(id);
        Musicista musicistaLoggato = this.musicistaService.findByUtenteId(id);
        if(musicistaLoggato.getId()!= found.getMittente().getId()) throw new UnauthorizedException("Non puoi cancellare feedback lasciati da altri utenti");
        this.repo.delete(found);
    }

    public Page<Feedback> getByDestinatarioId(UUID id,int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.findByDestinatarioId(found.getId(),pageable);
    }

}
