package federicopini.jammee.repos;

import federicopini.jammee.entities.Posizione;

import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosizioneRepo extends JpaRepository<Posizione, UUID> {
    Optional<Posizione> findByMusicistaId(UUID musicistaId);
    boolean existsByMusicistaId(UUID musicistaId);
}
