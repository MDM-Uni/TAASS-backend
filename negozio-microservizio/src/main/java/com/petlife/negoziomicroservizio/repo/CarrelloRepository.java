package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Carrello;
import org.springframework.data.repository.CrudRepository;

public interface CarrelloRepository extends CrudRepository<Carrello, Long> {
}
