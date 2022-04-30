package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Animale;
import com.petlife.negoziomicroservizio.model.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnimaleRepository extends CrudRepository<Animale, Long> {
    Optional<Animale> findByOrdiniId(long ordineId);
}
