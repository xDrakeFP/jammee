package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.competenza.CompetenzaDTO;
import federicopini.jammee.DTOs.competenza.UpdatedCompetenzaDTO;
import federicopini.jammee.entities.Competenza;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.CompetenzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/proficiency")
public class CompetenzaController {

    @Autowired
    private CompetenzaService service;

    @GetMapping("/{id}")
    public Competenza findById(UUID id){
        return this.service.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Competenza create(@RequestBody @Validated CompetenzaDTO body, @AuthenticationPrincipal Utente utente, BindingResult validationResult)
    {
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.createCompetenza(body,utente.getId());
    }

    @GetMapping("/{userId}")
    public Page<Competenza> getByUserId(@PathVariable UUID userId,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy)
    {
        return this.service.getByUserId(pageNumber, pageSize,sortBy,userId);
    }

    @GetMapping("/me")
    public Page<Competenza> getMine(@AuthenticationPrincipal Utente utente, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy)
    {
        return this.service.getByUserId(pageNumber,pageSize,sortBy,utente.getId());
    }

    @PatchMapping("/update")
    public Competenza updateCompetenza(@AuthenticationPrincipal Utente utente, @RequestBody @Validated UpdatedCompetenzaDTO body, BindingResult validationResult){
        return this.service.updateCompetenza(body,utente.getId());
    }

    @DeleteMapping("/delete/{strumentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID strumentoId, @AuthenticationPrincipal Utente utenteLoggato){
        this.service.deleteCompetenza(strumentoId,utenteLoggato.getId());
    }
}
