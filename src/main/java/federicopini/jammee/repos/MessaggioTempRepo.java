package federicopini.jammee.repos;

import federicopini.jammee.entities.MessaggioTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessaggioTempRepo extends JpaRepository<MessaggioTemp, UUID> {
}
