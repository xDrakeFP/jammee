package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.types.TipoStrumentoDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.types.TipoStrumento;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.types.TipoStrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/instrument-type")
public class TipoStrumentoController {

    @Autowired
    private TipoStrumentoService service;

    @GetMapping
    public Page<TipoStrumento> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.service.getAll(pageNumber,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public TipoStrumento findById(@PathVariable UUID id){
        return this.service.findById(id);
    }

    @GetMapping("/bytype")
    public Page<TipoStrumento> getAllByType(@RequestBody @Validated TipoStrumentoDTO body, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.getAllByType(body,pageNumber,pageSize,sortBy);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public TipoStrumento createTipoStrumento(@RequestBody @Validated TipoStrumentoDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.create(body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}")
    public TipoStrumento updateTipoStrumento(@PathVariable UUID id,@RequestBody @Validated TipoStrumentoDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.update(id,body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteTipoStrumento(@PathVariable UUID id){
        this.service.delete(id);
    }
}
