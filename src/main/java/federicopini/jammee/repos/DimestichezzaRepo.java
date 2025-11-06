package federicopini.jammee.repos;

import federicopini.jammee.entities.Dimestichezza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DimestichezzaRepo extends JpaRepository<Dimestichezza, UUID> {
}
