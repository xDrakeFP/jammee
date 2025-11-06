package federicopini.jammee.repos;

import federicopini.jammee.entities.Partecipazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartecipazioneRepo extends JpaRepository<Partecipazione, UUID> {
}
