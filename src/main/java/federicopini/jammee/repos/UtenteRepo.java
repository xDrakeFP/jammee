package federicopini.jammee.repos;

import federicopini.jammee.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtenteRepo extends JpaRepository<Utente, UUID> {
}
