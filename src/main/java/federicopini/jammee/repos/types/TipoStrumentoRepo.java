package federicopini.jammee.repos.types;

import federicopini.jammee.entities.types.TipoStrumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoStrumentoRepo extends JpaRepository<TipoStrumento, UUID> {
}
