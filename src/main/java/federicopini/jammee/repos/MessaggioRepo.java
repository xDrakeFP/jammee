package federicopini.jammee.repos;

import federicopini.jammee.entities.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessaggioRepo extends JpaRepository<Messaggio, UUID> {
}
