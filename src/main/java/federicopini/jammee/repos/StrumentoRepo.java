package federicopini.jammee.repos;

import federicopini.jammee.entities.Strumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StrumentoRepo extends JpaRepository<Strumento, UUID> {
    boolean existsByNome(String nome);
}
