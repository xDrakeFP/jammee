package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.chat.MessaggioTempDTO;
import federicopini.jammee.entities.MessaggioTemp;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.MessaggioTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessaggioController {
    @Autowired
    private MessaggioTempService messaggioTempService;

    @GetMapping("/{id}")
    public MessaggioTemp findById(@PathVariable UUID id){
        return this.messaggioTempService.findById(id);
    }

    @PostMapping("/send")
    public MessaggioTemp sendMessagio(@AuthenticationPrincipal Utente utente, @RequestBody @Validated MessaggioTempDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.messaggioTempService.create(utente.getId(),body);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMessaggio(@AuthenticationPrincipal Utente utente, @PathVariable UUID id){
        this.messaggioTempService.delete(utente,id);
    }
}
