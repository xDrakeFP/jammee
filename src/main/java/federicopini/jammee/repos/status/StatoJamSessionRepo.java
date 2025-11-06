package federicopini.jammee.repos.status;

import federicopini.jammee.entities.status.StatoJamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatoJamSessionRepo extends JpaRepository<StatoJamSession, UUID> {
}
