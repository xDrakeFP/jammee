package federicopini.jammee.repos.types;

import federicopini.jammee.entities.types.TipoStrumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TipoStrumentoRepo extends JpaRepository<TipoStrumento, UUID> {
    Optional<TipoStrumento> findByTipo(String tipo);
    Page<TipoStrumento> findById(UUID id, Pageable pageable);
    boolean existsByTipo(String tipo);
}
