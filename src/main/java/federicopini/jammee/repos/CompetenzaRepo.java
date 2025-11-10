package federicopini.jammee.repos;

import federicopini.jammee.entities.Competenza;
import federicopini.jammee.entities.Dimestichezza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompetenzaRepo extends JpaRepository<Competenza, UUID> {
    boolean existsByMusicistaIdAndStrumentoId(UUID musicistaId, UUID strumentoId);
    Page<Competenza> findByMusicistaId(UUID musicistaId, Pageable pageable);
    Optional<Competenza> findByMusicistaIdAndStrumentoId(UUID musicistaId, UUID strumentoId);
}
