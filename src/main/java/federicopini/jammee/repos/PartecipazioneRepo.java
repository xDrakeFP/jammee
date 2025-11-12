package federicopini.jammee.repos;

import federicopini.jammee.entities.Partecipazione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartecipazioneRepo extends JpaRepository<Partecipazione, UUID> {
    Page<Partecipazione> findByMusicistaId(UUID id, Pageable pageable);
    boolean existsByMusicistaIdAndJamSessionId(UUID musicistaId,UUID jamSessionId);
}
