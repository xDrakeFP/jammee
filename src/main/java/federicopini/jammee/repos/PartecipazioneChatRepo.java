package federicopini.jammee.repos;

import federicopini.jammee.entities.PartecipazioneChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartecipazioneChatRepo extends JpaRepository<PartecipazioneChat, UUID> {
}
