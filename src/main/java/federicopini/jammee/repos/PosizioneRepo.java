package federicopini.jammee.repos;

import federicopini.jammee.entities.Posizione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;

public interface PosizioneRepo extends JpaRepository<Posizione, UUID> {
    Optional<Posizione> findByMusicistaId(UUID musicistaId);
    boolean existsByMusicistaId(UUID musicistaId);

    @Query(value = """
        SELECT p.*,\s
        (6371 * acos(
            cos(radians(:lat)) *
            cos(radians(p.lat)) *
            cos(radians(p.lng) - radians(:lng)) +
            sin(radians(:lat)) *
            sin(radians(p.lat))
        )) AS distance
        FROM posizione p
        HAVING distance <= :maxKm
        ORDER BY distance ASC
       \s""", nativeQuery = true)
    Page<Posizione> findNearby(@Param("lat") double lat,
                               @Param("lng") double lng,
                               @Param("maxKm") double maxKm, Pageable pageable);
}
