package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.types.TipoJamSessionDTO;
import federicopini.jammee.entities.Dimestichezza;
import federicopini.jammee.entities.types.TipoJamSession;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.types.TipoJamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/jam")
public class JamSessionController {

    @Autowired
    private TipoJamSessionService tipoJamSessionService;

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

}
