package federicopini.jammee.repos;

import federicopini.jammee.entities.Competenza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompetenzaRepo extends JpaRepository<Competenza, UUID> {
    boolean existsByMusicistaIdAndStrumentoId(UUID musicistaId, UUID strumentoId);
}
