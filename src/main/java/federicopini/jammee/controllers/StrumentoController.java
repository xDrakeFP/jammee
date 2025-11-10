package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.strumento.StrumentoDTO;
import federicopini.jammee.entities.Dimestichezza;
import federicopini.jammee.entities.Strumento;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.StrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/instruments")
public class StrumentoController {
    @Autowired
    public StrumentoService service;

    @GetMapping("/{id}")
    public Strumento findById(UUID id){
        return this.service.findById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Strumento create(@RequestBody @Validated StrumentoDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.createStrumento(body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}")
    public Strumento update(@RequestBody @Validated StrumentoDTO body, @PathVariable UUID id, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.updateStrumento(id, body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/delete/{id}")
    public void delete(@PathVariable UUID id){
        this.service.deleteStrumento(id);
    }

}
