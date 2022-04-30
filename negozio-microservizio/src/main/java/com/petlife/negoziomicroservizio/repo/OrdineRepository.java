package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Ordine;
import org.springframework.data.repository.CrudRepository;

public interface OrdineRepository extends CrudRepository<Ordine, Long> {
    boolean existsByIndirizzoConsegnaId(long indirizzoId);
}
