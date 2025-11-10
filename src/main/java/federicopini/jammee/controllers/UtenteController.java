package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.utente.UpdatedTipoUtenteDTO;
import federicopini.jammee.DTOs.utente.UpdatedUtenteDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UtenteController {
    @Autowired
    private UtenteService service;

    @GetMapping
    public Page<Utente> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "username") String sortBy){
        return this.service.getAll(pageNumber, pageSize, sortBy);
    }

    @GetMapping("/{id}")
    public Utente findById(@PathVariable UUID id){
        return this.service.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable UUID id){
        this.service.deleteUser(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public Utente update(@PathVariable UUID id, @RequestBody UpdatedUtenteDTO body) {
        return this.service.updateUser(id, body);
    }

    @GetMapping("/me")
    public Utente getMe(@AuthenticationPrincipal Utente utenteLoggato) {
        return utenteLoggato;
    }

    @PatchMapping("/me")
    public Utente updateMe(@AuthenticationPrincipal Utente utenteLoggato, @RequestBody UpdatedUtenteDTO body){
        return this.service.updateUser(utenteLoggato.getId(), body);
    }

    @DeleteMapping("/me")
    public void deleteMe(@AuthenticationPrincipal Utente utenteLoggato){
        this.service.deleteUser(utenteLoggato.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/type/{id}")
    public Utente updateUserType(@PathVariable UUID id, @RequestBody UpdatedTipoUtenteDTO body){
        return this.service.updateTipoUtente(id,body);
    }

}
