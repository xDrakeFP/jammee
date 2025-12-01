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


import java.util.ArrayList;
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


    public Page<PosizioneConDistanzaDTO> getNearby(
            double lat,
            double lng,
            double maxKm,
            int pageNumber,
            int pageSize,
            UUID strumentoId,
            UUID genereId
    ) {
        System.out.println("genereId = " + genereId);
        System.out.println("strumentoId = " + strumentoId);
        int offset = pageNumber * pageSize;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("""
        SELECT sub.*, sub.distanza
        FROM (
            SELECT p.id, p.musicista_id, p.latitudine, p.longitudine, p.accuracy, p.timestamp,
                (6371 * acos(
                    cos(radians(?)) * cos(radians(p.latitudine)) *
                    cos(radians(p.longitudine) - radians(?)) +
                    sin(radians(?)) * sin(radians(p.latitudine))
                )) AS distanza
            FROM posizioni p
        ) AS sub
        WHERE 1 = 1
        """);

        params.add(lat);
        params.add(lng);
        params.add(lat);

        sql.append(" AND sub.distanza <= ? ");
        params.add(maxKm);

        if (strumentoId != null) {
            sql.append("""
            AND EXISTS (
                SELECT 1 FROM competenze c
                WHERE c.musicista_id = sub.musicista_id
                  AND c.strumento_id = ?
            )
        """);
            params.add(strumentoId);
        }

        if (genereId != null) {
            sql.append("""
            AND EXISTS (
                SELECT 1 FROM dimestichezze f
                WHERE f.musicista_id = sub.musicista_id
                  AND f.genere_id = ?
            )
        """);
            params.add(genereId);
        }

        sql.append(" ORDER BY sub.distanza ASC ");
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        List<PosizioneConDistanzaDTO> list = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> {
                    Posizione posizione = new Posizione();
                    posizione.setId(UUID.fromString(rs.getString("id")));
                    posizione.setMusicista(this.musicistaService.findById(UUID.fromString(rs.getString("musicista_id"))));
                    posizione.setLatitudine(rs.getObject("latitudine", Double.class));
                    posizione.setLongitudine(rs.getObject("longitudine", Double.class));
                    posizione.setAccuracy(rs.getObject("accuracy", Double.class));
                    posizione.setTimestamp(rs.getTimestamp("timestamp").toInstant());

                    double distanza = rs.getDouble("distanza");
                    return new PosizioneConDistanzaDTO(posizione, distanza);
                }
        );

        StringBuilder countSql = new StringBuilder();
        List<Object> countParams = new ArrayList<>();

        countSql.append("""
        SELECT COUNT(*) 
        FROM (
            SELECT p.id, p.musicista_id,
                (6371 * acos(
                    cos(radians(?)) * cos(radians(p.latitudine)) *
                    cos(radians(p.longitudine) - radians(?)) +
                    sin(radians(?)) * sin(radians(p.latitudine))
                )) AS distanza
            FROM posizioni p
        ) AS sub
        WHERE 1 = 1
    """);

        countParams.add(lat);
        countParams.add(lng);
        countParams.add(lat);

        countSql.append(" AND sub.distanza <= ? ");
        countParams.add(maxKm);

        if (strumentoId != null) {
            countSql.append("""
            AND EXISTS (
                SELECT 1 FROM competenze c
                WHERE c.musicista_id = sub.musicista_id
                  AND c.strumento_id = ?
            )
        """);
            countParams.add(strumentoId);
        }

        if (genereId != null) {
            countSql.append("""
            AND EXISTS (
                SELECT 1 FROM dimestichezze f
                WHERE f.musicista_id = sub.musicista_id
                  AND f.genere_id = ?
            )
        """);
            countParams.add(genereId);
        }

        Integer total = jdbcTemplate.queryForObject(countSql.toString(), countParams.toArray(), Integer.class);
        if (total == null) total = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        System.out.println("SQL: " + sql.toString());
        System.out.println("Params: " + params);
        return new PageImpl<>(list, pageable, total.longValue());
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
