package federicopini.jammee.repos;

import federicopini.jammee.entities.Strumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StrumentoRepo extends JpaRepository<Strumento, UUID> {
    boolean existsByNome(String nome);
    Page<Strumento> findByTipoId(UUID tipoId, Pageable pageable);
}
