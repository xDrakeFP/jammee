package federicopini.jammee.controllers;

import federicopini.jammee.DTOs.auth.LoginDTO;
import federicopini.jammee.DTOs.auth.LoginResponseDTO;
import federicopini.jammee.DTOs.utente.UtenteDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.ValidationException;
import federicopini.jammee.services.AuthService;
import federicopini.jammee.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body){
        return new LoginResponseDTO(authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    public Utente register(@RequestBody UtenteDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()) throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return this.utenteService.registerUser(body);
    }

}
