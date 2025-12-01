package federicopini.jammee.repos;

import federicopini.jammee.entities.MessaggioTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessaggioTempRepo extends JpaRepository<MessaggioTemp, UUID> {
    Page<MessaggioTemp> findByDestinatario_Id(UUID destinatarioId, Pageable pageable);
    Page<MessaggioTemp> findByMittente_Id(UUID MittenteId,Pageable pageable);

}
