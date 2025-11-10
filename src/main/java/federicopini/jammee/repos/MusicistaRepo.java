package federicopini.jammee.repos;

import federicopini.jammee.entities.Musicista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MusicistaRepo extends JpaRepository<Musicista, UUID> {
    Optional<Musicista> findByUtenteId(UUID id);

    boolean existsByUtenteId(UUID id);
}
