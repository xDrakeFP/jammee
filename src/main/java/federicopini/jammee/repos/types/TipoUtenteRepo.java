package federicopini.jammee.repos.types;

import federicopini.jammee.entities.types.TipoUtente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TipoUtenteRepo extends JpaRepository<TipoUtente, UUID> {
    Optional<TipoUtente> findByTipo(String tipo);
    boolean existsByTipo(String tipo);
}
