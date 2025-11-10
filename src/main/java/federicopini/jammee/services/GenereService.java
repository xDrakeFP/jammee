package federicopini.jammee.services;

import federicopini.jammee.DTOs.generi.GenereDTO;
import federicopini.jammee.entities.Genere;
import federicopini.jammee.entities.Utente;
import federicopini.jammee.exceptions.AlreadyExistingException;
import federicopini.jammee.exceptions.NotFoundException;
import federicopini.jammee.repos.GenereRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenereService {
    @Autowired
    private GenereRepo repo;

    public Genere findById(UUID id){
        return this.repo.findById(id).orElseThrow(()-> new NotFoundException("Nessun genere trovato con l'id fornito"));
    }

    public Page<Genere> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 30) pageSize = 30;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.repo.findAll(pageable);
    }

    public Genere addGenere(GenereDTO body){
        if(this.repo.existsByGenere(body.genere())) throw new AlreadyExistingException("Il genere esista già a database");
        Genere newGenere = new Genere(body.genere());
        return this.repo.save(newGenere);
    }

    public void deleteGenere(UUID id){
        this.repo.delete(this.findById(id));
    }

}
