package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.JamSession.JamSessionDTO;
import federicopini.jammee.DTOs.JamSession.UpdatedJamSessionDTO;
import federicopini.jammee.DTOs.status.StatoJamSessionDTO;
import federicopini.jammee.DTOs.types.TipoJamSessionDTO;
import federicopini.jammee.entities.Dimestichezza;
import federicopini.jammee.entities.JamSession;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.entities.status.StatoJamSession;
import federicopini.jammee.entities.types.TipoJamSession;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.JamSessionService;
import federicopini.jammee.services.status.StatoJamSessionService;
import federicopini.jammee.services.types.TipoJamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/jam")
public class JamSessionController {

    @Autowired
    private JamSessionService service;

    @Autowired
    private TipoJamSessionService tipoJamSessionService;

    @Autowired
    private StatoJamSessionService statoJamSessionService;

    @GetMapping("/{id}")
    public JamSession findById(@PathVariable UUID id){
        return this.service.findById(id);
    }

    @PostMapping("/create")
    public JamSession createJamSession(@AuthenticationPrincipal @Validated Utente utente, @RequestBody JamSessionDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.createJamSession(utente.getId(),body);
    }

    @PatchMapping("/update/{id}")
    public JamSession updateJamSession(@PathVariable UUID id, @RequestBody @Validated UpdatedJamSessionDTO body,@AuthenticationPrincipal Utente utente, BindingResult validationResult)
    {
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.updateJamSession(id,body,utente.getId());
    }

    @PatchMapping("/status/{id}")
    public JamSession updateStatusJamSession(@PathVariable UUID id,@RequestBody @Validated StatoJamSessionDTO body ,@AuthenticationPrincipal Utente utente){
        return this.service.updateStatus(id,body,utente.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteJamSession(@PathVariable UUID id,@AuthenticationPrincipal Utente utente){
        this.service.deleteJamSession(id, utente.getId());
    }

    @GetMapping("/types")
    public Page<TipoJamSession> getByUserId(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy)
    {
        return this.tipoJamSessionService.getAll(pageNumber,pageSize,sortBy);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/type/create")
    public TipoJamSession createTypeJam(@RequestBody @Validated TipoJamSessionDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.tipoJamSessionService.add(body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/type/edit/{id}")
    public TipoJamSession editTypeJam(@PathVariable UUID id, @RequestBody @Validated TipoJamSessionDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.tipoJamSessionService.edit(id,body);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/type/delete/{id}")
    public void deleteTypeJam(@PathVariable UUID id){
        this.tipoJamSessionService.delete(id);
    }

    @GetMapping("/statuses")
    public Page<StatoJamSession> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.statoJamSessionService.getAll(pageNumber,pageSize,sortBy);
    }

    @PostMapping("/status/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public StatoJamSession addNewStatusType(@RequestBody @Validated StatoJamSessionDTO body,BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.statoJamSessionService.add(body);
    }

    @PatchMapping("/status/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public StatoJamSession editStatusType(@PathVariable UUID id,@RequestBody @Validated StatoJamSessionDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.statoJamSessionService.edit(id,body);
    }

    @DeleteMapping("/status/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteStatusType(@PathVariable UUID id){
        this.statoJamSessionService.delete(id);
    }

}
