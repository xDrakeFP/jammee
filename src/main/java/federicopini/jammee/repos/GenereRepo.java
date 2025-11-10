package federicopini.jammee.repos;

import federicopini.jammee.entities.Genere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenereRepo extends JpaRepository<Genere, UUID> {
    boolean existsByGenere(String genere);
}
