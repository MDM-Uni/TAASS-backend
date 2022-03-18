package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
    Optional<Utente> findByCarrelloId(long carrelloId);

    Optional<Utente> findByOrdiniId(long ordineId);
}
