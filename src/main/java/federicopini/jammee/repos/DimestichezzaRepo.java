package federicopini.jammee.repos;

import federicopini.jammee.entities.Dimestichezza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DimestichezzaRepo extends JpaRepository<Dimestichezza, UUID> {
   boolean existsByMusicistaIdAndGenereId(UUID musicistaId, UUID genereId);
   Page<Dimestichezza> findByMusicistaId(UUID musicistaId, Pageable pageable);
   Optional<Dimestichezza> findByMusicistaIdAndGenereId(UUID musicistaId, UUID genereId);
}
