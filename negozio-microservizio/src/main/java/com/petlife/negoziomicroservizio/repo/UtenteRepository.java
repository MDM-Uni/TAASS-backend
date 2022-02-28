package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Utente;
import org.springframework.data.repository.CrudRepository;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
}
