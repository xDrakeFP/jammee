package federicopini.jammee.services;

import federicopini.jammee.DTOs.posizione.PosizioneConDistanzaDTO;
import federicopini.jammee.DTOs.posizione.PosizioneDTO;
import federicopini.jammee.entities.Musicista;
import federicopini.jammee.entities.Posizione;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.PosizioneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class PosizioneService {

    @Autowired
    private PosizioneRepo repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MusicistaService musicistaService;


    public Page<PosizioneConDistanzaDTO> getNearby(double lat, double lng, double maxKm, int pageNumber, int pageSize) {
        int offset = pageNumber * pageSize;

        String sql = """
        SELECT *
        FROM (
            SELECT p.*,
                   (6371 * acos(
                       cos(radians(?)) * cos(radians(p.latitudine)) *
                       cos(radians(p.longitudine) - radians(?)) +
                       sin(radians(?)) * sin(radians(p.latitudine))
                   )) AS distanza
            FROM posizioni p
        ) AS sub
        WHERE sub.distanza <= ?
        ORDER BY sub.distanza ASC
        LIMIT ? OFFSET ?
    """;

        List<PosizioneConDistanzaDTO> result = jdbcTemplate.query(
                sql,
                new Object[]{lat, lng, lat, maxKm, pageSize, offset},
                (rs, rowNum) -> {
                    Posizione posizione = new Posizione();
                    posizione.setId(UUID.fromString(rs.getString("id")));
                    posizione.setMusicista(this.musicistaService.findById(UUID.fromString(rs.getString("musicista_id"))));
                    posizione.setLatitudine(rs.getDouble("latitudine"));
                    posizione.setLongitudine(rs.getDouble("longitudine"));
                    posizione.setAccuracy(rs.getObject("accuracy", Double.class));
                    posizione.setTimestamp(rs.getTimestamp("timestamp").toInstant());

                    double distanza = rs.getDouble("distanza");

                    return new PosizioneConDistanzaDTO(posizione, distanza);
                }
        );

        String countSql = """
        SELECT COUNT(*)
        FROM (
            SELECT (6371 * acos(
                       cos(radians(?)) * cos(radians(p.latitudine)) *
                       cos(radians(p.longitudine) - radians(?)) +
                       sin(radians(?)) * sin(radians(p.latitudine))
                   )) AS distanza
            FROM posizioni p
        ) AS sub
        WHERE sub.distanza <= ?
    """;

        Integer total = jdbcTemplate.queryForObject(countSql, new Object[]{lat, lng, lat, maxKm}, Integer.class);

        return new PageImpl<>(result, PageRequest.of(pageNumber, pageSize), total);
    }





    public Posizione findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessuna posizione trovata con l'id indicato"));
    }

    public Page<Posizione> getAll(int pageNumber, int pageSize, String sortBy) {
        if(pageSize > 30 ) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }



    public Posizione findByUtenteId(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.findByMusicistaId(found.getId()).orElseThrow(()->new NotFoundException("Nessuna posizione trovata per l'utente con l'id indicato "));
    }

    public Posizione saveLocation(UUID utenteId, PosizioneDTO body){
        Musicista found = this.musicistaService.findByUtenteId(utenteId);
        if(this.repo.existsByMusicistaId(found.getId())) this.repo.delete(this.findByUtenteId(found.getId()));
        Posizione newPosizione = new Posizione(found,body.latitude(), body.longitude(), body.accuracy());
        return this.repo.save(newPosizione);
    }

    public boolean existsByMusicista(UUID id){
        Musicista found = this.musicistaService.findByUtenteId(id);
        return this.repo.existsByMusicistaId(found.getId());
    }


    public void deleteLocation(UUID id){

        Posizione foundPosizione = this.findByUtenteId(id);
        this.repo.delete(foundPosizione);
    }

}
