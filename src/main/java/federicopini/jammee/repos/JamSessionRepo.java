package federicopini.jammee.repos;

import federicopini.jammee.entities.JamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JamSessionRepo extends JpaRepository<JamSession, UUID> {
}
