package federicopini.jammee.repos;

import federicopini.jammee.entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRepo extends JpaRepository<Feedback, UUID> {
    Page<Feedback> findByDestinatarioId(UUID id, Pageable pageable);
}
