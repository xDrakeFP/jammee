package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.musicista.MusicistaDTO;
import federicopini.jammee.DTOs.utente.UpdatedUtenteDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.services.MusicistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/musician")
public class MusicistaController {
    @Autowired
    private MusicistaService service;

    @GetMapping
    public Page<Musicista> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.service.getAll(pageNumber,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public Musicista findById(@PathVariable UUID id){
        return this.service.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Musicista createMusicista(@AuthenticationPrincipal Utente utenteLoggato,@RequestBody MusicistaDTO body){
        return this.service.createMusicista(utenteLoggato.getId(),body);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable UUID id){
        this.service.deleteMusicista(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public Musicista update(@PathVariable UUID id, @RequestBody MusicistaDTO body) {
        return this.service.updateMusicista(id, body);
    }

    @GetMapping("/me")
    public Musicista getMe(@AuthenticationPrincipal Utente utenteLoggato) {
        return this.service.findByUtenteId(utenteLoggato.getId());
    }

    @PatchMapping("/me")
    public Musicista updateMe(@AuthenticationPrincipal Utente utenteLoggato, @RequestBody MusicistaDTO body){
        return this.service.updateMusicista(utenteLoggato.getId(), body);
    }

    @DeleteMapping("/me")
    public void deleteMe(@AuthenticationPrincipal Utente utenteLoggato){
        this.service.deleteMusicista(utenteLoggato.getId());
    }



}
