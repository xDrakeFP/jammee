package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.genere.GenereDTO;
import federicopini.jammee.entities.Genere;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.GenereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/genres")
public class GenereController {

    @Autowired
    private GenereService service;

    @GetMapping
    public Page<Genere> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "genere") String sortBy)
    {
        return this.service.getAll(pageNumber,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public Genere findById(@PathVariable UUID id){
        return this.service.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Genere addGenere(@RequestBody @Validated GenereDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.addGenere(body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenere(@PathVariable UUID id){
        this.service.deleteGenere(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public Genere updateGenere(@PathVariable UUID id, @RequestBody @Validated GenereDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.updateGenere(id,body);
    }

}
