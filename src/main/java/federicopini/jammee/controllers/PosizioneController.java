package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.posizione.PosizioneConDistanzaDTO;
import federicopini.jammee.DTOs.posizione.PosizioneDTO;
import federicopini.jammee.entities.Posizione;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.PosizioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/location")
@Slf4j
public class PosizioneController {

    @Autowired
    private PosizioneService service;

    @GetMapping("/me")
    public Posizione findMyLocation(@AuthenticationPrincipal Utente utente){
        return this.service.findByUtenteId(utente.getId());
    }

    @GetMapping("/all")
    public Page<Posizione> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy)
    {
        return this.service.getAll(pageNumber,pageSize,sortBy);
    }

    @GetMapping("/nearby")
    public Page<PosizioneConDistanzaDTO> getNearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "50") double maxKm,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return this.service.getNearby(lat, lng, maxKm, pageNumber, pageSize);
    }

    @PostMapping("/me")
    public Posizione savePosizione(@AuthenticationPrincipal Utente utente, @RequestBody @Validated PosizioneDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.saveLocation(utente.getId(), body);
    }

    @GetMapping("/user/{id}")
    public Posizione getByUserId(@PathVariable UUID id){
        return this.service.findByUtenteId(id);
    }

    @GetMapping("/has-position")
    public boolean existsByMusicistaId(@AuthenticationPrincipal Utente utente){
        return this.service.existsByMusicista(utente.getId());
    }

    @DeleteMapping("/me")
    public void deletePosizione(@AuthenticationPrincipal Utente utente){
        this.service.deleteLocation(utente.getId());
    }

}
