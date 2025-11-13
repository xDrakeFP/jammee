package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.feedback.FeedbackDTO;
import federicopini.jammee.DTOs.feedback.UpdatedFeedbackDTO;
import federicopini.jammee.entities.Feedback;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    @GetMapping("/{id}")
    public Feedback findById(@PathVariable UUID id) {
        return this.service.findById(id);
    }

    @PostMapping("/create")
    public Feedback addFeedback(@AuthenticationPrincipal Utente utente, @RequestBody @Validated FeedbackDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.createFeedback(utente,body);
    }

    @PatchMapping("/edit/{id}")
    public Feedback editFeedback(@AuthenticationPrincipal Utente utente, @PathVariable UUID id, @RequestBody @Validated UpdatedFeedbackDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.service.updateFeedback(utente,id,body);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFeedback(@AuthenticationPrincipal Utente utente,@PathVariable UUID id){
        this.service.deleteFeedback(utente,id);
    }

    @GetMapping("/by-user/{id}")
    public Page<Feedback> getByUser(@PathVariable UUID id,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.service.getByDestinatarioId(id,pageNumber,pageSize,sortBy);
    }

    @GetMapping("/me")
    public Page<Feedback> getMine(@AuthenticationPrincipal Utente utente,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.service.getByDestinatarioId(utente.getId(),pageNumber,pageSize,sortBy);
    }
}
