package federicopini.jammee.repos.status;

import federicopini.jammee.entities.status.StatoJamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface StatoJamSessionRepo extends JpaRepository<StatoJamSession, UUID> {
    boolean existsByStato(String stato);
    Optional<StatoJamSession> findByStato(String stato);
}
