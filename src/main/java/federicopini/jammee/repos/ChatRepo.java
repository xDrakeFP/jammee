package federicopini.jammee.repos;

import federicopini.jammee.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepo extends JpaRepository<Chat, UUID> {
}
