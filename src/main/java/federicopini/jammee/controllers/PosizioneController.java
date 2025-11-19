package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.posizione.PosizioneDTO;
import federicopini.jammee.entities.Posizione;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.PosizioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class PosizioneController {

    @Autowired
    private PosizioneService service;

    @GetMapping("/me")
    public Posizione findMyLocation(@AuthenticationPrincipal Utente utente){
        return this.service.findByMusicistaId(utente.getId());
    }

    @PostMapping("/me")
    public Posizione savePosizione(@AuthenticationPrincipal Utente utente, @RequestBody @Validated PosizioneDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.saveLocation(utente.getId(), body);
    }

    @DeleteMapping("/me")
    public void deletePosizione(@AuthenticationPrincipal Utente utente){
        this.service.deleteLocation(utente.getId());
    }

}
