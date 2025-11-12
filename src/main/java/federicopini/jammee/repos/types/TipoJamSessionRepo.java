package federicopini.jammee.repos.types;

import federicopini.jammee.entities.types.TipoJamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TipoJamSessionRepo extends JpaRepository<TipoJamSession, UUID> {
    boolean existsByTipo(String tipo);
}
