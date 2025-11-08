package federicopini.jammee.services;

import federicopini.jammee.DTOs.auth.LoginDTO;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteService service;
    @Autowired
    private JWTTools tools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body){

    }
}
