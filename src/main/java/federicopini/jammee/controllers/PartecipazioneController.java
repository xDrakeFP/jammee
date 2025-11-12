package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.partecipazione.PartecipazioneDTO;
import federicopini.jammee.entities.Partecipazione;
import federicopini.jammee.entities.Strumento;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.PartecipazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/participation")
public class PartecipazioneController {

    @Autowired
    private PartecipazioneService service;

    @GetMapping("/{id}")
    public Partecipazione findById(UUID id){
        return this.service.findById(id);
    }

    @PostMapping("/add")
    public Partecipazione addPartecipazione(@RequestBody @Validated PartecipazioneDTO body, @AuthenticationPrincipal Utente utente, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.addPartecipazione(body,utente);
    }

    @PatchMapping("/confirm/{id}")
    public Partecipazione confirmPartecipazione(@PathVariable UUID id, @AuthenticationPrincipal Utente utente){
        return this.service.confirmPartecipazione(id,utente);
    }

    @PatchMapping("/undo/{id}")
    public Partecipazione undoPartecipazione(@PathVariable UUID id, @AuthenticationPrincipal Utente utente){
        return this.service.undoPartecipazione(id,utente);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePartecipazione(@PathVariable UUID id, @AuthenticationPrincipal Utente utente){
        this.service.deletePartecipazione(id,utente);
    }

    @GetMapping("/by-user-id")
    public Page<Partecipazione> getByUserId(@AuthenticationPrincipal Utente utente,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.service.getByMusicistaId(utente.getId(),pageNumber,pageSize,sortBy);
    }
}
