package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.dimestichezza.DimestichezzaDTO;
import federicopini.jammee.DTOs.dimestichezza.UpdatedDimestichezzaDTO;
import federicopini.jammee.entities.Dimestichezza;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.DimestichezzaService;
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
@RequestMapping("/familiarity")
public class DimestichezzaController {

    @Autowired
    private DimestichezzaService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Dimestichezza create(@RequestBody @Validated DimestichezzaDTO body, @AuthenticationPrincipal Utente utenteLoggato, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.addDimestichezza(body, utenteLoggato.getId());
    }

    @GetMapping("/me")
    public Page<Dimestichezza> getMine(@AuthenticationPrincipal Utente utenteLoggato,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "nome") String sortBy){
        return this.service.getMine(pageNumber,pageSize,sortBy,utenteLoggato.getId());
    }

    @PatchMapping("/update")
    public Dimestichezza updateDimestichezza(@AuthenticationPrincipal Utente utenteLoggato, @RequestBody @Validated UpdatedDimestichezzaDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.updateDimestichezza(body,utenteLoggato.getId());
    }

    @DeleteMapping("/delete/{genereId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID genereId, @AuthenticationPrincipal Utente utenteLoggato){
        this.service.deleteDimestichezza(genereId,utenteLoggato.getId());
    }


}
